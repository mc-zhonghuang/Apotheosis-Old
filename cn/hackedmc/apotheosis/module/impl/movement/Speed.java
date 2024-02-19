package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.impl.movement.speed.*;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.TeleportEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import org.lwjgl.input.Keyboard;

/**
 * @author Patrick (implementation)
 * @since 10/19/2021
 */
@Rise
@ModuleInfo(name = "module.movement.speed.name", description = "module.movement.speed.description", category = Category.MOVEMENT)
public class Speed extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaSpeed("Vanilla", this))
            .add(new StrafeSpeed("Strafe", this))
            .add(new InteractSpeed("Interact", this))
            .add(new VulcanSpeed("Vulcan", this))
            .add(new WatchdogSpeed("Watchdog", this))
            .add(new NCPSpeed("NCP", this))
            .add(new FuncraftSpeed("Funcraft", this))
            .add(new VerusSpeed("Verus", this))
            .add(new BlocksMCSpeed("BlocksMC", this))
            .add(new MineMenClubSpeed("MineMenClub", this))
            .add(new KoksCraftSpeed("KoksCraft", this))
            .add(new LegitSpeed("Legit", this))
            .setDefault("Vanilla");

    private final BooleanValue disableOnTeleport = new BooleanValue("Disable on Teleport", this, false);
    private final BooleanValue stopOnDisable = new BooleanValue("Stop on Disable", this, false);
    public Speed () {
        this.setKeyCode(Keyboard.KEY_V);
    }
    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1.0F;

        if (stopOnDisable.getValue()) {
            MoveUtil.stop();
        }
    }

    @EventLink()
    public final Listener<TeleportEvent> onTeleport = event -> {
        if (disableOnTeleport.getValue()) {
            this.toggle();
        }
    };
}