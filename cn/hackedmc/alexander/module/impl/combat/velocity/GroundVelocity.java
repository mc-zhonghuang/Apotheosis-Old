package cn.hackedmc.alexander.module.impl.combat.velocity;


import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.impl.combat.Velocity;
import cn.hackedmc.alexander.newevent.impl.input.MoveInputEvent;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.NumberValue;

public final class GroundVelocity extends Mode<Velocity>  {

    private final NumberValue delay = new NumberValue("Delay", this, 1, 0, 20, 1);

    private int ticks;

    public GroundVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !mc.thePlayer.isSwingInProgress) return;

        if (ticks == delay.getValue().intValue()) {
            mc.thePlayer.onGround = true;
        }

        ticks++;
    };

    @EventLink()
    public final Listener<MoveInputEvent> onMove = event -> {
        if (ticks == delay.getValue().intValue() + 1) {
            event.setJump(false);
        }
    };
}
