package cn.hackedmc.alexander.module.impl.combat;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.combat.criticals.*;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.module.impl.combat.criticals.*;
import cn.hackedmc.alexander.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.combat.criticals.name", description = "module.combat.criticals.description", category = Category.COMBAT)
public final class Criticals extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new PacketCriticals("Packet", this))
            .add(new EditCriticals("Edit", this))
            .add(new NoGroundCriticals("No Ground", this))
            .add(new GrimACCriticals("GrimAC", this))
            .add(new NCPCriticals("NCP", this))
            .add(new WatchdogCriticals("Watchdog", this))
            .setDefault("Packet");
}
