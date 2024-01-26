package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;

public class NCPSprint extends Mode<Scaffold> {
    public NCPSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        event.setOnGround(false);
        mc.gameSettings.keyBindSprint.setPressed(true);
        mc.thePlayer.setSprinting(true);
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            mc.thePlayer.motionX *= 0.95;
            mc.thePlayer.motionZ *= 0.95;
        } else {
            mc.thePlayer.motionX *= 0.99;
            mc.thePlayer.motionZ *= 0.99;
        }
    };
}
