package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Auth
 * @since 3/02/2022
 */
public class RoundNoFall extends Mode<NoFall> {

    public RoundNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        final double rounded = MoveUtil.roundToGround(event.getPosY());
        final float distance = FallDistanceComponent.distance;

        if (distance > 3 && Math.abs(rounded - event.getPosY()) < 0.005) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, rounded, mc.thePlayer.posZ);
            event.setOnGround(true);
            event.setPosY(rounded);
        }
    };
}