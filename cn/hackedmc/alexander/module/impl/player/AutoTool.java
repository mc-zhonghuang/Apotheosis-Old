package cn.hackedmc.alexander.module.impl.player;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.impl.player.SlotComponent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.Priorities;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.newevent.impl.other.BlockDamageEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.player.SlotUtil;
import cn.hackedmc.alexander.value.impl.ModeValue;
import cn.hackedmc.alexander.value.impl.SubMode;
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

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK && lastSlot != -1 && InstanceAccess.mc.thePlayer.inventory.currentItem != lastSlot && blockBreak > 0) {
            event.setCancelled();
            InstanceAccess.mc.getNetHandler().addToSendQueueUnregistered(new C09PacketHeldItemChange(lastSlot));
            InstanceAccess.mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, ((C07PacketPlayerDigging) packet).getPosition(), ((C07PacketPlayerDigging) packet).getFacing()));
            InstanceAccess.mc.getNetHandler().addToSendQueueUnregistered(new C09PacketHeldItemChange(InstanceAccess.mc.thePlayer.inventory.currentItem));
        }

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C09PacketHeldItemChange && blockBreak > 0) {
            if (((C09PacketHeldItemChange) packet).getSlotId() == lastSlot || ((C09PacketHeldItemChange) packet).getSlotId() == InstanceAccess.mc.thePlayer.inventory.currentItem)
                event.setCancelled();
        }

        if (InstanceAccess.mc.objectMouseOver != null && InstanceAccess.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && InstanceAccess.mc.gameSettings.keyBindAttack.isPressed()) {
            blockBreak = 3;
            blockPos = InstanceAccess.mc.objectMouseOver.getBlockPos();
        }

        if (mode.getValue().getName().equalsIgnoreCase("Watchdog") && packet instanceof C08PacketPlayerBlockPlacement && blockBreak > 0) {
            ((C08PacketPlayerBlockPlacement) packet).setStack(InstanceAccess.mc.thePlayer.getHeldItem());
        }
    };

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        switch (InstanceAccess.mc.objectMouseOver.typeOfHit) {
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
            InstanceAccess.mc.thePlayer.inventory.currentItem = lastSlot;
        blockBreak--;
    };
}