package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.movement.noweb.*;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.movement.noweb.name", description = "module.movement.noweb.description", category = Category.MOVEMENT)
public class NoWeb extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaNoWeb("Vanilla", this))
            .add(new GrimACNoWeb("GrimAC", this))
            .setDefault("Vanilla");
}