package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.impl.player.SlotComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.NumberValue;
import net.minecraft.item.ItemBlock;

/**
 * @author Alan
 * @since 29/01/2021
 */

@Rise
@ModuleInfo(name = "module.ghost.fastplace.name", description = "module.ghost.fastplace.description", category = Category.GHOST)
public class FastPlace extends Module {

    private final NumberValue delay = new NumberValue("Delay", this, 0, 0, 3, 1);

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (SlotComponent.getItemStack() != null && SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
            mc.rightClickDelayTimer = Math.min(mc.rightClickDelayTimer, this.delay.getValue().intValue());
        }
    };
}