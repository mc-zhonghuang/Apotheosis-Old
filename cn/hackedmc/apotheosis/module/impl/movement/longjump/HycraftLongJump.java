package cn.hackedmc.apotheosis.module.impl.movement.longjump;

import cn.hackedmc.apotheosis.module.impl.movement.LongJump;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;

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
