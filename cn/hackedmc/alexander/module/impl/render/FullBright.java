package cn.hackedmc.alexander.module.impl.render;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.render.fullbright.EffectFullBright;
import cn.hackedmc.alexander.module.impl.render.fullbright.GammaFullBright;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.ModeValue;

/**
 * @author Patrick
 * @since 10/19/2021
 */
@Rise
@ModuleInfo(name = "module.render.fullbright.name", description = "module.render.fullbright.description", category = Category.RENDER)
public final class FullBright extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new GammaFullBright("Gamma", this))
            .add(new EffectFullBright("Effect", this))
            .setDefault("Gamma");
}