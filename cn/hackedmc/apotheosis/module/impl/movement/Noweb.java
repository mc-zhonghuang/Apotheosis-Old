package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.movement.noweb.*;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.movement.Noweb.name", description = "module.movement.Noweb.description", category = Category.MOVEMENT)
public class Noweb extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaNoweb("Vanilla", this))
            .setDefault("Vanilla");
}