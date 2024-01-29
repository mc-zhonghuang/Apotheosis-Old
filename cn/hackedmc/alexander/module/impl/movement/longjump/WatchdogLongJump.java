package cn.hackedmc.alexander.module.impl.movement.longjump;

import cn.hackedmc.alexander.component.impl.player.BlinkComponent;
import cn.hackedmc.alexander.module.impl.movement.LongJump;
import cn.hackedmc.alexander.component.impl.render.NotificationComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.MoveEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author LvZiQiao
 * @since 1/27/2024
 */

public class WatchdogLongJump extends Mode<LongJump> {
    private int jumpTime = 0;

    public WatchdogLongJump(String name, LongJump parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        jumpTime = 0;
    }

    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
        if (jumpTime < 4)
            event.zeroXZ();
    };

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (jumpTime < 4) {
            if (mc.thePlayer.onGround) {
                jumpTime++;
                if (jumpTime == 4) {
                    mc.thePlayer.motionY = 3.0;
                    BlinkComponent.blinking = true;
                } else {
                    mc.thePlayer.jump();
                }
            }
            if (jumpTime != 4) event.setOnGround(false);
        } else {
            if (mc.thePlayer.onGround) {
                BlinkComponent.dispatch();
                BlinkComponent.blinking = false;
                this.toggle();
            }
        }
    };
}