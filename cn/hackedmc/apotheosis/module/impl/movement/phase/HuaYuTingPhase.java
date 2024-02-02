package cn.hackedmc.apotheosis.module.impl.movement.phase;

import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.component.impl.render.NotificationComponent;
import cn.hackedmc.apotheosis.module.impl.movement.Phase;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.util.vector.Vector3d;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGlass;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class HuaYuTingPhase extends Mode<Phase> {
    public HuaYuTingPhase(String name, Phase parent) {
        super(name, parent);
    }
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Normal"))
            .add(new SubMode("Boost"))
            .setDefault("Normal");
    private Vector3d startPlayer;
    private boolean phasing;
    private BlockPos startPos;
    private int boostTick;

    @Override
    public void onEnable() {
        if (mode.getValue().getName().equalsIgnoreCase("Normal")) {
            startPlayer = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            startPos = new BlockPos(mc.thePlayer).down();
            phasing = true;
            BlinkComponent.setExempt(C08PacketPlayerBlockPlacement.class);
            BlinkComponent.blinking = true;
        }

        boostTick = 0;
    }

    @EventLink
    private final Listener<WorldChangeEvent> onWorld = event -> {
        phasing = false;
        boostTick = 0;
    };

    @Override
    public void onDisable() {
        if (startPos != null && !(mc.theWorld.getBlockState(startPos).getBlock() instanceof BlockAir)) {
            BlinkComponent.packets.forEach(packet -> {
                if (packet instanceof C03PacketPlayer) {
                    final C03PacketPlayer wrapped = (C03PacketPlayer) packet;

                    if (wrapped.moving) {
                        wrapped.x = startPlayer.getX();
                        wrapped.y = startPlayer.getY();
                        wrapped.z = startPlayer.getZ();
                    }
                }

                mc.getNetHandler().addToSendQueueUnregistered(packet);
            });
            BlinkComponent.packets.clear();

            mc.thePlayer.setPosition(startPlayer.getX(), startPlayer.getY(), startPlayer.getZ());
        }
        BlinkComponent.blinking = false;
        startPos = null;
    }

    @EventLink
    private final Listener<BlockAABBEvent> onBlockAABB = event -> {
        if (mode.getValue().getName().equalsIgnoreCase("Normal") && phasing)
            event.setBoundingBox(null);
    };

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mode.getValue().getName().equalsIgnoreCase("Normal")) {
            if (mc.thePlayer.posY + 3.1 < startPos.getY()) {
                phasing = false;

                if (mc.theWorld.getBlockState(startPos).getBlock() instanceof BlockAir) {
                    BlinkComponent.blinking = false;
                    this.getParent().toggle();
                    NotificationComponent.post("Phase", "Operation successful!");
                }
            }
        } else {
            if (!phasing) startPos = new BlockPos(mc.thePlayer).up(2);

            if (mc.theWorld.getBlockState(startPos).getBlock() instanceof BlockGlass) {
                if (!phasing) {
                    phasing = true;
                    BlinkComponent.setExempt(C08PacketPlayerBlockPlacement.class);
                    BlinkComponent.blinking = true;
                    boostTick = 0;
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ);
                }
                boostTick++;

                if (boostTick == 5) {
                    MoveUtil.strafe(5);
                }

                if (boostTick == 1000) {
                    BlinkComponent.packets.clear();
                    BlinkComponent.blinking = false;
                    mc.thePlayer.sendChatMessage("/hub");
                }
            } else {
                BlinkComponent.blinking = false;
            }
        }
    };
}
