package cn.hackedmc.alexander.module.impl.movement.step;

import cn.hackedmc.alexander.module.impl.movement.Step;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Alan
 * @since 22/3/2022
 */

public class JumpStep extends Mode<Step> {

    public JumpStep(String name, Step parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.jump();
        }
    };
}