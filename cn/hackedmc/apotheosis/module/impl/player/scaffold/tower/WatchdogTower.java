package cn.hackedmc.apotheosis.module.impl.player.scaffold.tower;

import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.Mode;

public class WatchdogTower extends Mode<Scaffold> {
    public WatchdogTower(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (!mc.gameSettings.keyBindJump.isKeyDown()) return;

        if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.42 - Math.random() / 10000;
        } else if (mc.thePlayer.motionY >= 0.16 && mc.thePlayer.motionY <= 0.17) {
            mc.thePlayer.motionY = 0;
        }
    };
}
