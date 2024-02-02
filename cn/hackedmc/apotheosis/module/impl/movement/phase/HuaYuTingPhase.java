package cn.hackedmc.apotheosis.module.impl.movement.phase;

import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.component.impl.render.NotificationComponent;
import cn.hackedmc.apotheosis.module.impl.movement.Phase;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.apotheosis.util.vector.Vector3d;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class HuaYuTingPhase extends Mode<Phase> {
    public HuaYuTingPhase(String name, Phase parent) {
        super(name, parent);
    }
    private Vector3d startPlayer;
    private boolean phasing;
    private BlockPos startPos;

    @Override
    public void onEnable() {
        startPlayer = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        startPos = new BlockPos(mc.thePlayer).down();
        phasing = true;
        BlinkComponent.blinking = true;
        BlinkComponent.setExempt(C08PacketPlayerBlockPlacement.class);
    }

    @Override
    public void onDisable() {
        if (!(mc.theWorld.getBlockState(startPos).getBlock() instanceof BlockAir)) {
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
    }

    @EventLink
    private final Listener<BlockAABBEvent> onBlockAABB = event -> {
        if (phasing)
            event.setBoundingBox(null);
    };

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.posY + 3.1 < startPos.getY()) {
            phasing = false;

            if (mc.theWorld.getBlockState(startPos).getBlock() instanceof BlockAir) {
                BlinkComponent.blinking = false;
                this.getParent().toggle();
                NotificationComponent.post("Phase", "Operation successful!");
            }
        }
    };
}
