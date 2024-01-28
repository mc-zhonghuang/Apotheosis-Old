package cn.hackedmc.alexander.module.impl.movement;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.movement.inventorymove.BufferAbuseInventoryMove;
import cn.hackedmc.alexander.module.impl.movement.inventorymove.CancelInventoryMove;
import cn.hackedmc.alexander.module.impl.movement.inventorymove.NormalInventoryMove;
import cn.hackedmc.alexander.module.impl.movement.inventorymove.WatchdogInventoryMove;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.module.impl.movement.inventorymove.*;
import cn.hackedmc.alexander.value.impl.ModeValue;

/**
 * @author Alan
 * @since 20/10/2021
 */

@Rise
@ModuleInfo(name = "module.movement.inventorymove.name", description = "module.movement.inventorymove.description", category = Category.MOVEMENT)
public class InventoryMove extends Module {

    private final ModeValue bypassMode = new ModeValue("Bypass Mode", this)
            .add(new NormalInventoryMove("Normal", this))
            .add(new BufferAbuseInventoryMove("Buffer Abuse", this))
            .add(new CancelInventoryMove("Cancel", this))
            .add(new WatchdogInventoryMove("Watchdog", this))
            .setDefault("Normal");
}
