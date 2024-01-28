package cn.hackedmc.alexander.module.impl.combat.criticals;

import cn.hackedmc.alexander.module.impl.combat.Criticals;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.value.Mode;

public final class NoGroundCriticals extends Mode<Criticals> {

    public NoGroundCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        event.setOnGround(false);
    };
}
