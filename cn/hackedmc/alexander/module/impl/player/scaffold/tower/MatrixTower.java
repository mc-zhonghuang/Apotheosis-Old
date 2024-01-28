package cn.hackedmc.alexander.module.impl.player.scaffold.tower;

import cn.hackedmc.alexander.module.impl.player.Scaffold;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.value.Mode;

public class MatrixTower extends Mode<Scaffold> {

    public MatrixTower(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.isBlockUnder(2, false) && mc.thePlayer.motionY < 0.2) {
            mc.thePlayer.motionY = 0.42F;
            event.setOnGround(true);
        }
    };
}
