package cn.hackedmc.alexander.module.impl.player;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

@Rise
@ModuleInfo(name = "module.player.inventorysync.name", description = "module.player.inventorysync.description", category = Category.PLAYER)
public class InventorySync extends Module {

    public short action;

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {

        final Packet<?> packet = event.getPacket();

        if (packet instanceof S32PacketConfirmTransaction) {
            final S32PacketConfirmTransaction wrapper = (S32PacketConfirmTransaction) packet;
            final Container inventory = InstanceAccess.mc.thePlayer.inventoryContainer;

            if (wrapper.getWindowId() == inventory.windowId) {
                this.action = wrapper.getActionNumber();

                if (this.action > 0 && this.action < inventory.transactionID) {
                    inventory.transactionID = (short) (this.action + 1);
                }
            }
        }
    };
}