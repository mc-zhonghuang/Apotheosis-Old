package cn.hackedmc.alexander.module.impl.player.antivoid;

import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.module.impl.player.AntiVoid;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.NumberValue;

public class CollisionAntiVoid extends Mode<AntiVoid> {

    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);

    public CollisionAntiVoid(String name, AntiVoid parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (FallDistanceComponent.distance > distance.getValue().intValue() && !PlayerUtil.isBlockUnder() && mc.thePlayer.posY + mc.thePlayer.motionY < Math.floor(mc.thePlayer.posY)) {
            mc.thePlayer.motionY = Math.floor(mc.thePlayer.posY) - mc.thePlayer.posY;
            if (mc.thePlayer.motionY == 0) {
                mc.thePlayer.onGround = true;
                event.setOnGround(true);
            }
        }
    };
}