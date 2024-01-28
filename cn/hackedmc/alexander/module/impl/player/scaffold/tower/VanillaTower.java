package cn.hackedmc.alexander.module.impl.player.scaffold.tower;

import cn.hackedmc.alexander.module.impl.player.Scaffold;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.value.Mode;

public class VanillaTower extends Mode<Scaffold> {

    public VanillaTower(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.blockNear(2)) {
            mc.thePlayer.motionY = 0.42F;
        }
    };
}
