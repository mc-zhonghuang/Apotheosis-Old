package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

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
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && MoveUtil.isMoving()) {
            PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        event.setCancelled(true);
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapped = (S02PacketChat) packet;

            if (wrapped.isChat()) {
                final String text = wrapped.getChatComponent().getUnformattedText();

                if (text.equalsIgnoreCase("建筑高度限制是 256 个方块") || text.equalsIgnoreCase("Height limit for building is 256 blocks"))
                    event.setCancelled();
            }
        }
    };

    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemBow || (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && !ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())))) {
            final C08PacketPlayerBlockPlacement wrapped = (C08PacketPlayerBlockPlacement) packet;

            if (wrapped.getPlacedBlockDirection() == 255 && wrapped.getPosition().equals(new BlockPos(-1, -1, -1))) {
                event.setCancelled();
                mc.getNetHandler().addToSendQueueUnregistered(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, 1000, mc.thePlayer.posZ), 0, mc.thePlayer.getHeldItem(), 0, 0, 0));
            }
        }
    };
}
