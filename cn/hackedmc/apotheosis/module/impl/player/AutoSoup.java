package cn.hackedmc.apotheosis.module.impl.player;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.component.impl.player.SlotComponent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
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
