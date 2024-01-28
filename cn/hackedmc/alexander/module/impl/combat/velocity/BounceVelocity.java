package cn.hackedmc.alexander.module.impl.combat.velocity;


import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.module.impl.combat.Velocity;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.NumberValue;

public final class BounceVelocity extends Mode<Velocity> {

    private final NumberValue tick = new NumberValue("Tick", this, 0, 0, 6, 1);
    private final BooleanValue vertical = new BooleanValue("Vertical", this, false);
    private final BooleanValue horizontal = new BooleanValue("Horizontal", this, false);

    public BounceVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !mc.thePlayer.isSwingInProgress) return;

        if (mc.thePlayer.hurtTime == 9 - this.tick.getValue().intValue()) {
            if (this.horizontal.getValue()) {
                if (MoveUtil.isMoving()) {
                    MoveUtil.strafe();
                } else {
                    mc.thePlayer.motionZ *= -1;
                    mc.thePlayer.motionX *= -1;
                }
            }

            if (this.vertical.getValue()) {
                mc.thePlayer.motionY *= -1;
            }
        }
    };
}
