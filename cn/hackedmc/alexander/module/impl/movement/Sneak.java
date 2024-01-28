package cn.hackedmc.alexander.module.impl.movement;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.movement.sneak.HoldSneak;
import cn.hackedmc.alexander.module.impl.movement.sneak.NCPSneak;
import cn.hackedmc.alexander.module.impl.movement.sneak.StandardSneak;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.ModeValue;

/**
 * @author Auth
 * @since 25/06/2022
 */
@Rise
@ModuleInfo(name = "module.movement.sneak.name", description = "module.movement.sneak.description", category = Category.MOVEMENT)
public class Sneak extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new StandardSneak("Standard", this))
            .add(new HoldSneak("Hold", this))
            .add(new NCPSneak("NCP", this))
            .setDefault("Standard");
}