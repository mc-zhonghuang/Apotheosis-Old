package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.ghost.wtap.LegitWTap;
import cn.hackedmc.alexander.module.impl.ghost.wtap.SilentWTap;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.ModeValue;
import cn.hackedmc.alexander.value.impl.NumberValue;

/**
 * @author Alan
 * @since 29/01/2021
 */

@Rise
@ModuleInfo(name = "module.ghost.wtap.name", description = "module.ghost.wtap.description", category = Category.GHOST)
public class WTap extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new LegitWTap("Legit", this))
            .add(new SilentWTap("Silent", this))
            .setDefault("Legit");

    public final NumberValue chance = new NumberValue("WTap Chance", this, 100, 0, 100, 1);
}