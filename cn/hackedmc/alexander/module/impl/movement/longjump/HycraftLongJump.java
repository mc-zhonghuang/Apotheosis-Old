package cn.hackedmc.alexander.module.impl.movement.longjump;

import cn.hackedmc.alexander.module.impl.movement.LongJump;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.value.Mode;

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