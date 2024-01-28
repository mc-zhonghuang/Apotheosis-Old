package cn.hackedmc.alexander.module.impl.combat;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.combat.regen.VanillaRegen;
import cn.hackedmc.alexander.module.impl.combat.regen.WorldGuardRegen;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.combat.regen.name", description = "module.combat.regen.description", category = Category.COMBAT)
public final class Regen extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaRegen("Vanilla", this))
            .add(new WorldGuardRegen("World Guard", this))
            .setDefault("Vanilla");
}
