package cn.hackedmc.apotheosis.module.impl.combat.velocity;

import cn.hackedmc.apotheosis.component.impl.player.RotationComponent;
import cn.hackedmc.apotheosis.module.impl.combat.KillAura;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.module.impl.combat.Velocity;
import cn.hackedmc.apotheosis.util.RayCastUtil;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.viamcp.ViaMCP;

public class GrimACVelocity extends Mode<Velocity> {
    public GrimACVelocity(String name, Velocity parent) {
        super(name, parent);
    }
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Block Spoof"))
            .add(new SubMode("Attack Reduce"))
            .add(new SubMode("1.17+"))
            .setDefault("Block Spoof");
    private final BooleanValue legitSprint = new BooleanValue("Legit Sprint", this, false, () -> !mode.getValue().getName().equalsIgnoreCase("Attack Reduce"));
    private final BooleanValue rayCast = new BooleanValue("Ray cast", this, false, () -> !mode.getValue().getName().equalsIgnoreCase("Attack Reduce"));
    private int lastSprint = -1;

    @Override
    public void onDisable() {
        lastSprint = -1;
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mode.getValue().getName().equalsIgnoreCase("attack reduce") && legitSprint.getValue()) {
            if (lastSprint == 0) {
                lastSprint--;
                if (!mc.thePlayer.isSprinting())
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            } else if (lastSprint > 0) {
                lastSprint--;
                if (mc.thePlayer.onGround && !mc.thePlayer.isSprinting()) {
                    lastSprint = -1;
                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                }
            }
        }
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapped = (S12PacketEntityVelocity) packet;

            if (wrapped.getEntityID() == mc.thePlayer.getEntityId()) {
                switch (mode.getValue().getName().toLowerCase()) {
                    case "block spoof": {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                        mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
                        mc.timer.lastSyncSysClock += 1;
                        event.setCancelled();

                        break;
                    }
                    case "1.17+": {
                        mc.getNetHandler().addToSendQueueUnregistered(new C03PacketPlayer.C06PacketPlayerPosLook(
                                mc.thePlayer.posX,
                                mc.thePlayer.posY,
                                mc.thePlayer.posZ,
                                RotationComponent.rotations.x,
                                RotationComponent.rotations.y,
                                mc.thePlayer.onGround
                        ));
                        mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
                        event.setCancelled();

                        break;
                    }
                    case "attack reduce": {
                        Entity entity = null;

                        if (rayCast.getValue()) {
                            final MovingObjectPosition position = RayCastUtil.rayCast(RotationComponent.rotations, KillAura.INSTANCE.range.getValue().doubleValue());

                            if (position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
                                entity = position.entityHit;
                        } else {
                            entity = KillAura.INSTANCE.target;
                        }

                        if (entity != null) {
                            event.setCancelled();

                            if (!EntityPlayerSP.serverSprintState) {
                                if (legitSprint.getValue()) {
                                    if (lastSprint < 0) mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                                    lastSprint = 2;
                                } else {
                                    mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                                }
                            }

                            for (int i = 0;i < 8;i++) {
                                if (ViaMCP.getInstance().getVersion() <= 47) mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                                if (ViaMCP.getInstance().getVersion() > 47) mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            }

                            double velocityX = wrapped.motionX / 8000.0;
                            double velocityZ = wrapped.motionZ / 8000.0;

                            if (MathHelper.sqrt_double(velocityX * velocityX * velocityZ * velocityZ) <= 5F) {
                                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                            } else {
                                mc.thePlayer.motionX = velocityX * 0.15;
                                mc.thePlayer.motionZ = velocityZ * 0.15;
                            }

                            mc.thePlayer.motionY = wrapped.motionY / 8000.0;

                            if (!EntityPlayerSP.serverSprintState && !legitSprint.getValue())
                                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        }

                        break;
                    }
                }
            }
        }
    };
}
