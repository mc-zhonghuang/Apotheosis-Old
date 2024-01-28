package cn.hackedmc.alexander.module.impl.movement.noslow;

import cn.hackedmc.alexander.module.impl.movement.NoSlow;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.NumberValue;

/**
 * @author Alan
 * @since 18/11/2021
 */

public class PredictionNoSlow extends Mode<NoSlow> {

    private final NumberValue amount = new NumberValue("Amount", this, 2, 2, 5, 1);

    public PredictionNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {

        if (mc.thePlayer.onGroundTicks % this.amount.getValue().intValue() != 0) {
            event.setCancelled(true);
        }
    };
}