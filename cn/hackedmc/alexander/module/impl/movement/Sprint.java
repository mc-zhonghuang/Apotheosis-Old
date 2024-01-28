package cn.hackedmc.alexander.module.impl.movement;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.Type;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.Priorities;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.value.impl.BooleanValue;

/**
 * @author Auth
 * @since 20/10/2021
 */
@ModuleInfo(name = "module.movement.sprint.name", description = "module.movement.sprint.description", category = Category.MOVEMENT)
public class Sprint extends Module {
    private final BooleanValue legit = new BooleanValue("Legit", this, true, () -> Client.CLIENT_TYPE != Type.RISE);

    @EventLink(value = Priorities.LOW)
    public final Listener<StrafeEvent> onStrafe = event -> {

        mc.gameSettings.keyBindSprint.setPressed(true);

        if (Client.CLIENT_TYPE != Type.RISE) return;

        if (mc.thePlayer.omniSprint && MoveUtil.isMoving() && !legit.getValue()) {
            mc.thePlayer.setSprinting(true);
        }

        mc.thePlayer.omniSprint = !legit.getValue() && MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally &&
                !mc.thePlayer.isSneaking() && !mc.thePlayer.isUsingItem();
    };

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(mc.gameSettings.keyBindSprint.isKeyDown());
        mc.thePlayer.omniSprint = false;
    }
}