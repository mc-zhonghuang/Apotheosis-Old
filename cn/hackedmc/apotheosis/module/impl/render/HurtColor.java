package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.HurtRenderEvent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;

/**
 * @author Alan
 * @since 28/05/2022
 */

@ModuleInfo(name = "module.render.hurtcolor.name", description = "module.render.hurtcolor.description", category = Category.RENDER)
public final class HurtColor extends Module {

    private final BooleanValue oldDamage = new BooleanValue("1.7 Damage Animation", this, true);


    @EventLink()
    public final Listener<HurtRenderEvent> onHurtRender = event -> {
        event.setOldDamage(oldDamage.getValue());
    };
}