package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
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
        /*if (mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) */event.setCancelled(true);
    };

    @EventLink
    public final Listener<PacketSendEvent> onPrePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown() && (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemBucketMilk || (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && !ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())) || mc.thePlayer.getHeldItem().getItem() instanceof ItemBow))) {
                if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !(((C08PacketPlayerBlockPlacement) packet).getPosition().equals(new BlockPos(-1, -1, -1)))) return;
                event.setCancelled();
                MovingObjectPosition position = mc.thePlayer.rayTraceCustom(mc.playerController.getBlockReachDistance(), mc.thePlayer.rotationYaw, 90f);
                if (position == null) return;
                RotationComponent.setRotations(new Vector2f(mc.thePlayer.rotationYaw, 90f), 10, MovementFix.OFF);
                sendUseItem(position);
            }
        }
    };

    private void sendUseItem(MovingObjectPosition mouse) {
        final float facingX = (float) (mouse.hitVec.xCoord - (double) mouse.getBlockPos().getX());
        final float facingY = (float) (mouse.hitVec.yCoord - (double) mouse.getBlockPos().getY());
        final float facingZ = (float) (mouse.hitVec.zCoord - (double) mouse.getBlockPos().getZ());

        PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(mouse.getBlockPos(), mouse.sideHit.getIndex(), mc.thePlayer.getHeldItem(), facingX, facingY, facingZ));
    }
}
