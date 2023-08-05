package com.alan.clients.module.impl.player;

import com.alan.clients.api.Rise;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

@Rise
@ModuleInfo(name = "module.player.autosoup.name", description = "module.player.autosoup.description", category = Category.PLAYER)
public class AutoSoup extends Module {
    private final NumberValue health = new NumberValue("Health", this, 15, 1, 20, 1);

    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (mc.thePlayer.getHealth() <= health.getValue().floatValue()) {
            for (int i = 0; i < 9; i++) {
                if (mc.thePlayer.getHealth() > health.getValue().floatValue())
                    break;

                final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                if (stack == null)
                    continue;

                if (stack.getItem() instanceof ItemSoup) {
                    SlotComponent.setSlot(i);

                    PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
                }
            }
        }
    };
}
