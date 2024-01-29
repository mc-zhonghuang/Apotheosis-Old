package cn.hackedmc.apotheosis.module.impl.player.flagdetector;

import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.module.impl.player.FlagDetector;
import cn.hackedmc.apotheosis.value.Mode;


public class MathGround extends Mode<FlagDetector> {

    public MathGround(String name, FlagDetector parent) {
        super(name, parent);
    }

    @EventLink(value = Priorities.VERY_LOW)
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (event.isOnGround() && event.getPosY() % (1 / 64f) != 0) {
            getParent().fail("Math Ground");
        }

    };
}
