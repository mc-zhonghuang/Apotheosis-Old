package cn.hackedmc.alexander.module.impl.player;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.impl.player.BadPacketsComponent;
import cn.hackedmc.alexander.component.impl.player.SlotComponent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.math.MathUtil;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.value.impl.BoundsNumberValue;
import cn.hackedmc.alexander.value.impl.NumberValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import util.time.StopWatch;

@Rise
@ModuleInfo(name = "module.player.autohead.name", description = "module.player.autohead.description", category = Category.PLAYER)
public class AutoHead extends Module {

    private final NumberValue health = new NumberValue("Health", this, 15, 1, 20, 1);
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 500, 1000, 50, 5000, 50);

    private final StopWatch stopWatch = new StopWatch();

    private long nextUse;


    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (this.getModule(Scaffold.class).isEnabled()) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            final ItemStack stack = InstanceAccess.mc.thePlayer.inventory.getStackInSlot(i);

            if (stack == null) {
                continue;
            }

            final Item item = stack.getItem();

            if (item instanceof ItemSkull) {
                if (InstanceAccess.mc.thePlayer.getHealth() > this.health.getValue().floatValue()) {
                    continue;
                }

                SlotComponent.setSlot(i);

                if (!BadPacketsComponent.bad() && stopWatch.finished(nextUse)) {
                    PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));

                    nextUse = Math.round(MathUtil.getRandom(delay.getValue().longValue(), delay.getSecondValue().longValue()));
                    stopWatch.reset();
                }
            }
        }
    };
}