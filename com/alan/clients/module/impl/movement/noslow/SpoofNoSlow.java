package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class SpoofNoSlow extends Mode<NoSlow> {
    public SpoofNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        if (mc.thePlayer.itemInUse != null && mc.thePlayer.itemInUse.getItem() instanceof ItemSword) event.setCancelled();
    };

    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement) {
            final C08PacketPlayerBlockPlacement wrapper = (C08PacketPlayerBlockPlacement) packet;

            if (wrapper.getStack() != null && wrapper.getStack().getItem() instanceof ItemSword)
                event.setCancelled();
        }
    };
}
