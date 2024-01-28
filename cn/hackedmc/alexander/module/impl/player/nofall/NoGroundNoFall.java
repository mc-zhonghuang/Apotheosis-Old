package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Alan
 * @since 3/02/2022
 */
public class NoGroundNoFall extends Mode<NoFall> {

    public NoGroundNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        event.setOnGround(false);
        event.setPosY(event.getPosY() + Math.random() / 100000000000000000000f);
    };
}