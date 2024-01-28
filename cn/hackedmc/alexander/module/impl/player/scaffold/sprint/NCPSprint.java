package cn.hackedmc.alexander.module.impl.player.scaffold.sprint;

import cn.hackedmc.alexander.module.impl.player.Scaffold;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.potion.Potion;

public class NCPSprint extends Mode<Scaffold> {
    public NCPSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        event.setOnGround(false);
        mc.gameSettings.keyBindSprint.setPressed(true);
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            mc.thePlayer.motionX *= 0.95;
            mc.thePlayer.motionZ *= 0.95;
        } else {
            mc.thePlayer.motionX *= 0.99;
            mc.thePlayer.motionZ *= 0.99;
        }
    };
}
