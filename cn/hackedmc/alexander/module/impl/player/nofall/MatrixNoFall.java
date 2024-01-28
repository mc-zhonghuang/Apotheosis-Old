package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Alan
 * @since 3/02/2022
 */
public class MatrixNoFall extends Mode<NoFall> {

    public MatrixNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        float distance = FallDistanceComponent.distance;

        if (PlayerUtil.isBlockUnder()) {
            if (distance > 2) {
                MoveUtil.strafe(0.19);
            }

            if (distance > 3 && MoveUtil.speed() < 0.2) {
                event.setOnGround(true);
                distance = 0;
            }
        }

        FallDistanceComponent.distance = distance;
    };
}