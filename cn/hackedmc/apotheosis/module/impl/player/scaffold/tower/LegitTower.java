package cn.hackedmc.apotheosis.module.impl.player.scaffold.tower;

import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.apotheosis.value.Mode;

public class LegitTower extends Mode<Scaffold> {
    // Bypasses jump delay, holding down space is slower than this
    public LegitTower(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }

        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    };
}
