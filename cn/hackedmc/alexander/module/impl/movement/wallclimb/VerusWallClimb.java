package cn.hackedmc.alexander.module.impl.movement.wallclimb;

import cn.hackedmc.alexander.module.impl.movement.WallClimb;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Nicklas
 * @since 05.06.2022
 */
public class VerusWallClimb extends Mode<WallClimb> {

    public VerusWallClimb(String name, WallClimb parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (InstanceAccess.mc.thePlayer.isCollidedHorizontally) {
            if (InstanceAccess.mc.thePlayer.ticksExisted % 2 == 0) {
                InstanceAccess.mc.thePlayer.jump();
            }
        }
    };
}