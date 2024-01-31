package cn.hackedmc.apotheosis.module.impl.player.scaffold.tower;

import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.value.Mode;

public class WatchdogTower extends Mode<Scaffold> {
    public WatchdogTower(String name, Scaffold parent) {
        super(name, parent);
    }

    private int towerTick = 0;

    @Override
    public void onEnable() {
        towerTick = 0;
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.blockNear(2)) {
            if (MoveUtil.isMoving()) {
                towerTick++;

                if (mc.thePlayer.onGround)
                    towerTick = 0;

                mc.thePlayer.motionY = 0.41965;
                MoveUtil.strafe(0.265);

                if (towerTick == 1)
                    mc.thePlayer.motionY = 0.33;
                else if (towerTick == 2)
                    mc.thePlayer.motionY = 1 - mc.thePlayer.posY % 1;
                else if (towerTick >= 3)
                    towerTick = 0;
            } else {
                towerTick = 0;
                if (mc.thePlayer.onGround) mc.thePlayer.jump();
            }
        } else {
            towerTick = 0;
        }
    };
}
