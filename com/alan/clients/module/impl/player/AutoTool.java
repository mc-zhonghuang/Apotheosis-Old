package com.alan.clients.module.impl.player;

import com.alan.clients.api.Rise;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.Priorities;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.BlockDamageEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author Alan (made good code)
 * @since 24/06/2023
 */

@Rise
@ModuleInfo(name = "module.player.autotool.name", description = "module.player.autotool.description", category = Category.PLAYER)
public class AutoTool extends Module {
    public ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Basic"))
            .add(new SubMode("Spoof"))
            .add(new SubMode("Watchdog"))
            .setDefault("Basic");
    private int slot, lastSlot = -1;
    private int blockBreak;
    private BlockPos blockPos;

    @EventLink(Priorities.VERY_HIGH)
    public final Listener<BlockDamageEvent> onBlockDamage = event -> {
        blockBreak = 3;
        blockPos = event.getBlockPos();
    };

    @EventLink
    public final Listener<PacketSendEvent> onPrePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK && lastSlot != -1 && mc.thePlayer.inventory.currentItem != lastSlot && blockBreak > 0) {
            event.setCancelled();
            mc.getNetHandler().addToSendQueueUnregistered(new C09PacketHeldItemChange(lastSlot));
            mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, ((C07PacketPlayerDigging) packet).getPosition(), ((C07PacketPlayerDigging) packet).getFacing()));
            mc.getNetHandler().addToSendQueueUnregistered(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C09PacketHeldItemChange && blockBreak > 0) {
            if (((C09PacketHeldItemChange) packet).getSlotId() == lastSlot || ((C09PacketHeldItemChange) packet).getSlotId() == mc.thePlayer.inventory.currentItem)
                event.setCancelled();
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.gameSettings.keyBindAttack.isPressed()) {
            blockBreak = 3;
            blockPos = mc.objectMouseOver.getBlockPos();
        }

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C08PacketPlayerBlockPlacement && blockBreak > 0) {
            ((C08PacketPlayerBlockPlacement) packet).setStack(mc.thePlayer.getHeldItem());
        }
    };

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        switch (mc.objectMouseOver.typeOfHit) {
            case BLOCK:
                if (blockPos != null && blockBreak > 0) {
                    slot = SlotUtil.findTool(blockPos);
                } else {
                    slot = -1;
                }
                break;

            case ENTITY:
                slot = SlotUtil.findSword();
                break;

            default:
                slot = -1;
                break;
        }

        if (lastSlot != -1) {
            SlotComponent.setSlot(lastSlot);
        } else if (slot != -1) {
            SlotComponent.setSlot(slot);
        }

        lastSlot = slot;
        if (mode.getValue().getName().equalsIgnoreCase("Basic") && lastSlot != -1)
            mc.thePlayer.inventory.currentItem = lastSlot;
        blockBreak--;
    };
}