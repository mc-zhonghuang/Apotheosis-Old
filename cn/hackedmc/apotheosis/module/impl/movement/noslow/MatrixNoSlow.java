package cn.hackedmc.apotheosis.module.impl.movement.noslow;

import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.component.impl.player.SlotComponent;
import cn.hackedmc.apotheosis.module.impl.movement.NoSlow;
import cn.hackedmc.apotheosis.newevent.CancellableEvent;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MatrixNoSlow extends Mode<NoSlow> {
    public MatrixNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    private int blockingTick = 0;

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.isUsingItem()) {
            if (blockingTick >= 3) {
                mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                release();
                PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                blockingTick = 0;
            } else {
                BlinkComponent.blinking = true;
                blockingTick++;
            }
        } else {
            release();
            BlinkComponent.blinking = false;
            blockingTick = 0;
        }
    };

    @EventLink
    private final Listener<SlowDownEvent> onSlowDown = CancellableEvent::setCancelled;

    private static void release() {
        BlinkComponent.packets.forEach(PacketUtil::sendNoEvent);
        BlinkComponent.packets.clear();
    }
}
