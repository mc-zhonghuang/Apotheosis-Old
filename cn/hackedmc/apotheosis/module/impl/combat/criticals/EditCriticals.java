package cn.hackedmc.apotheosis.module.impl.combat.criticals;

import cn.hackedmc.apotheosis.module.impl.combat.Criticals;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.AttackEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import util.time.StopWatch;

public final class EditCriticals extends Mode<Criticals> {

    private final NumberValue delay = new NumberValue("Delay", this, 500, 0, 1000, 50);

    private final double[] VALUES = new double[]{0.0005D, 0.0001D};
    private final StopWatch stopwatch = new StopWatch();

    private boolean attacked;
    private int ticks;

    public EditCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (mc.thePlayer.onGround && attacked) {
            ticks++;

            switch (ticks) {
                case 1: {
                    event.setPosY(event.getPosY() + VALUES[0]);
                    break;
                }

                case 2: {
                    event.setPosY(event.getPosY() + VALUES[1]);
                    attacked = false;
                    break;
                }
            }

            event.setOnGround(false);
        } else {
            attacked = false;
            ticks = 0;
        }
    };

    @EventLink()
    public final Listener<AttackEvent> onAttackEvent = event -> {
        if (mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && stopwatch.finished(delay.getValue().longValue())) {
            mc.thePlayer.onCriticalHit(event.getTarget());

            stopwatch.reset();
            attacked = true;
        }
    };
}
