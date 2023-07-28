package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class HuaYuTingSprint extends Mode<Scaffold> {
    public HuaYuTingSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.offGroundTicks >= 2) {
            mc.gameSettings.keyBindSprint.setPressed(false);
            mc.thePlayer.setSprinting(false);
        }
        if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
            mc.gameSettings.keyBindSprint.setPressed(true);
            mc.thePlayer.setSprinting(true);
        }
    };
}
