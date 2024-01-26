package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;

public class HycraftLongJump extends Mode<LongJump> {
    public HycraftLongJump(String name, LongJump parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        mc.thePlayer.jump();
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        mc.thePlayer.motionY += 0.05999;
        MoveUtil.strafe(MoveUtil.speed() * 1.08f);
    };
}
