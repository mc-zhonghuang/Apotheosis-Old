package cn.hackedmc.apotheosis.module.impl.movement.noslow;

import cn.hackedmc.apotheosis.module.impl.movement.NoSlow;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PostMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class WatchdogNoSlow extends Mode<NoSlow> {
    public WatchdogNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && MoveUtil.isMoving()) {
//            PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            PacketUtil.sendNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
            PacketUtil.sendNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    };

    @EventLink()
    public final Listener<PostMotionEvent> onPostMotionEvent = event -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && MoveUtil.isMoving()) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                PacketUtil.send(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            else if (mc.thePlayer.ticksExisted % 3 == 0)
                PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.DOWN.getIndex(), null, 0, 0, 0));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        event.setCancelled(true);
    };
}
