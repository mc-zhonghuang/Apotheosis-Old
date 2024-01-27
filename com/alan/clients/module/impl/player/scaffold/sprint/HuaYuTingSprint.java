package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.client.settings.GameSettings;

public class HuaYuTingSprint extends Mode<Scaffold> {
    private boolean needStop = false;

    public HuaYuTingSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if ((mc.thePlayer.offGroundTicks < 3 || mc.thePlayer.motionY > 0) && MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally &&
                !mc.thePlayer.isSneaking() && !mc.thePlayer.isUsingItem()) {
            mc.gameSettings.keyBindSprint.setPressed(true);
            needStop = true;
        } else {
            mc.gameSettings.keyBindForward.setPressed(!needStop && GameSettings.isKeyDown(mc.gameSettings.keyBindForward));
            mc.gameSettings.keyBindSprint.setPressed(false);
            mc.thePlayer.setSprinting(false);
            needStop = false;
        }
    };

    @Override
    public void onEnable() {
        mc.gameSettings.keyBindSprint.setPressed(true);
    }
}
