package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.impl.movement.inventorymove.*;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.value.impl.ModeValue;

/**
 * @author Alan
 * @since 20/10/2021
 */

@Rise
@ModuleInfo(name = "module.movement.inventorymove.name", description = "module.movement.inventorymove.description", category = Category.MOVEMENT)
public class InventoryMove extends Module {
    public static InventoryMove INSTANCE;
    private final ModeValue bypassMode = new ModeValue("Bypass Mode", this)
            .add(new NormalInventoryMove("Normal", this))
            .add(new GrimACInventoryMove("GrimAC", this))
            .add(new BufferAbuseInventoryMove("Buffer Abuse", this))
            .add(new CancelInventoryMove("Cancel", this))
            .add(new WatchdogInventoryMove("Watchdog", this))
            .setDefault("GrimAC");

    public InventoryMove() {
        INSTANCE = this;
    }
}
