package cn.hackedmc.alexander.module.impl.movement;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.movement.step.*;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.module.impl.movement.step.*;
import cn.hackedmc.alexander.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.movement.step.name", description = "module.movement.step.description", category = Category.MOVEMENT)
public class Step extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaStep("Vanilla", this))
            .add(new WatchdogStep("Watchdog", this))
            .add(new NCPStep("NCP", this))
            .add(new NCPPacketLessStep("NCP Packetless", this))
            .add(new VulcanStep("Vulcan", this))
            .add(new MatrixStep("Matrix", this))
            .add(new JumpStep("Jump", this))
            .setDefault("Vanilla");
}