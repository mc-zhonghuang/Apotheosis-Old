package cn.hackedmc.alexander.module.impl.movement.wallclimb;

import cn.hackedmc.alexander.module.impl.movement.WallClimb;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Alan
 * @since 05.06.2022
 */

public class MineMenClubWallClimb extends Mode<WallClimb> {

    private boolean hitHead;

    public MineMenClubWallClimb(String name, WallClimb parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (InstanceAccess.mc.thePlayer.isCollidedHorizontally && !hitHead && InstanceAccess.mc.thePlayer.ticksExisted % 3 == 0) {
            InstanceAccess.mc.thePlayer.motionY = MoveUtil.jumpMotion();
        }

        if (InstanceAccess.mc.thePlayer.isCollidedVertically) {
            hitHead = !InstanceAccess.mc.thePlayer.onGround;
        }
    };
}