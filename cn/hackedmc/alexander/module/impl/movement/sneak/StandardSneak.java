package cn.hackedmc.alexander.module.impl.movement.sneak;

import cn.hackedmc.alexander.module.impl.movement.Sneak;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Auth
 * @since 25/06/2022
 */

public class StandardSneak extends Mode<Sneak> {

    public StandardSneak(String name, Sneak parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        mc.thePlayer.movementInput.sneak = mc.thePlayer.sendQueue.doneLoadingTerrain;
    };
}