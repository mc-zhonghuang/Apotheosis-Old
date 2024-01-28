package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.render.MouseOverEvent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.NumberValue;
import org.lwjgl.input.Mouse;

/**
 * @author Alan
 * @since 29/01/2021
 */
@Rise
@ModuleInfo(name = "module.ghost.hitbox.name", description = "module.ghost.hitbox.description", category = Category.GHOST)
public class HitBox extends Module {
    public final NumberValue expand = new NumberValue("Expand Amount", this, 0, 0, 6, 0.01);
    private final BooleanValue effectRange = new BooleanValue("Effect range", this, true);

    private int exempt;

    @EventLink()
    public final Listener<MouseOverEvent> onMouseOver = event -> {
        if (Mouse.isButtonDown(1)) {
            exempt = 1;
        }

        if (exempt > 0) return;

        event.setExpand(this.expand.getValue().floatValue());

        if (!this.effectRange.getValue()) {
            event.setRange(event.getRange() - expand.getValue().doubleValue());
        }
    };

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        exempt--;
    };
}