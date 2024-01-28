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

public class VariableNoSlow extends Mode<NoSlow> {

    private final NumberValue multiplier = new NumberValue("Multiplier", this, 0.8, 0.2, 1, 0.05);

    public VariableNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        event.setForwardMultiplier(multiplier.getValue().floatValue());
        event.setStrafeMultiplier(multiplier.getValue().floatValue());
    };
}