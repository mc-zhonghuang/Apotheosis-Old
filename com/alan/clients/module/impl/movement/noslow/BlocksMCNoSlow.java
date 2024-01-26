package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class BlocksMCNoSlow extends Mode<NoSlow> {
    public BlocksMCNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = CancellableEvent::setCancelled;

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.getHeldItem() == null || !MoveUtil.isMoving() || !mc.thePlayer.isUsingItem()) return;

        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex() % 8 + 1));
            PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex()));
        } else {
            PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    };
}
