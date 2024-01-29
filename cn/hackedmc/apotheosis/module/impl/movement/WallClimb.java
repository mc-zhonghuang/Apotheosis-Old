package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.movement.wallclimb.*;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

/**
 * @author Alan
 * @since 20/10/2021
 */
@Rise
@ModuleInfo(name = "module.movement.wallclimb.name", description = "module.movement.wallclimb.description", category = Category.MOVEMENT)
public class WallClimb extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VulcanWallClimb("Vulcan", this))
            .add(new VerusWallClimb("Verus", this))
            .add(new GrimACWallClimb("GrimAC", this))
            .add(new KauriWallClimb("Kauri", this))
            .add(new WatchdogWallClimb("Watchdog", this))
            .add(new MineMenClubWallClimb("MineMenClub", this))
            .setDefault("Vulcan");
}