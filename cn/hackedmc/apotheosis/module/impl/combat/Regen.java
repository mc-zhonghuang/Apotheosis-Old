package cn.hackedmc.apotheosis.module.impl.combat;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.impl.combat.regen.HigherVersionRegen;
import cn.hackedmc.apotheosis.module.impl.combat.regen.VanillaRegen;
import cn.hackedmc.apotheosis.module.impl.combat.regen.WorldGuardRegen;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.combat.regen.name", description = "module.combat.regen.description", category = Category.COMBAT)
public final class Regen extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaRegen("Vanilla", this))
            .add(new WorldGuardRegen("World Guard", this))
            .add(new HigherVersionRegen("1.17+", this))
            .setDefault("Vanilla");
}
