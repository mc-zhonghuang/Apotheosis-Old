package cn.hackedmc.apotheosis.module.impl.movement.wallclimb;

import cn.hackedmc.apotheosis.module.impl.movement.WallClimb;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;

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