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
    private int ticks = 0;
    private int jumpTime = 0;

    public WatchdogLongJump(String name, LongJump parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ticks = 0;
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
                mc.thePlayer.jump();
            }
            if (jumpTime != 4) event.setOnGround(false);
        } else {
            ticks++;
            if (ticks == 10) {
                BlinkComponent.blinking = true;
                MoveUtil.strafe(1);
                mc.thePlayer.jump();
            } else if (ticks > 10 && mc.thePlayer.onGround) {
                getParent().toggle();
                BlinkComponent.dispatch();
                BlinkComponent.blinking = false;
                NotificationComponent.post("Watchdog long jump", "After the jump.");
                this.ticks = 0;
            }
        }
    };
}