package cn.hackedmc.apotheosis.module.impl.player.flagdetector;

import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.module.impl.player.FlagDetector;
import cn.hackedmc.apotheosis.value.Mode;


public class Strafe extends Mode<FlagDetector> {

    public Strafe(String name, FlagDetector parent) {
        super(name, parent);
    }

    @EventLink(value = Priorities.VERY_LOW)
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (mc.thePlayer.offGroundTicks <= 1 || event.isOnGround() || mc.thePlayer.ticksSinceVelocity == 1 ||
                mc.thePlayer.capabilities.isFlying || mc.thePlayer.isCollidedHorizontally ||
                mc.thePlayer.ticksSinceTeleport == 1 || mc.thePlayer.ticksSincePlayerVelocity <= 20) return;

        double moveFlying = 0.02599999835384377;
        double friction = 0.9100000262260437;

        double lastDeltaX = Math.abs(mc.thePlayer.lastMotionX) * friction;
        double lastDeltaZ = Math.abs(mc.thePlayer.lastMotionZ) * friction;

        double deltaX = Math.abs(mc.thePlayer.motionX);
        double deltaZ = Math.abs(mc.thePlayer.motionZ);

        if (Math.abs(lastDeltaX - deltaX) > moveFlying || Math.abs(lastDeltaZ - deltaZ) > moveFlying) {
            getParent().fail("Strafe");
        }

    };
}
