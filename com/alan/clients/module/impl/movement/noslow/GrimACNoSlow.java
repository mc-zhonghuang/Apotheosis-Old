package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class GrimACNoSlow extends Mode<NoSlow> {
    public GrimACNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.thePlayer.isUsingItem() || this.getModule(KillAura.class).blocking) {
            PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
            PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = CancellableEvent::setCancelled;
}
