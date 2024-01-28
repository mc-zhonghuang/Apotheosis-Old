package cn.hackedmc.alexander.module.impl.render;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.render.esp.ChamsESP;
import cn.hackedmc.alexander.module.impl.render.esp.GlowESP;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.BooleanValue;

@Rise
@ModuleInfo(name = "module.render.esp.name", description = "module.render.esp.description", category = Category.RENDER)
public final class ESP extends Module {

    private BooleanValue glowESP = new BooleanValue("Glow", this, false, new GlowESP("", this));
    private BooleanValue chamsESP = new BooleanValue("Chams", this, false, new ChamsESP("", this));

}
