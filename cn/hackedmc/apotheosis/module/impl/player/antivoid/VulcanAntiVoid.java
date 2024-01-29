package cn.hackedmc.apotheosis.module.impl.player.antivoid;

import cn.hackedmc.apotheosis.module.impl.player.AntiVoid;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.TeleportEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;

/**
 * @author Strikeless
 * @since 18.03.2022
 */
public class VulcanAntiVoid extends Mode<AntiVoid> {

    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);

    private boolean teleported;

    public VulcanAntiVoid(String name, AntiVoid parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (mc.thePlayer.fallDistance > distance.getValue().floatValue()) {
            event.setPosY(event.getPosY() - event.getPosY() % 0.015625);
            event.setOnGround(true);

            mc.thePlayer.motionY = -0.08D;

            MoveUtil.stop();
        }

        if (teleported) {
            MoveUtil.stop();
            teleported = false;
        }
    };

    @EventLink()
    public final Listener<TeleportEvent> onTeleport = event -> {

        if (mc.thePlayer.fallDistance > distance.getValue().floatValue()) {
            teleported = true;
        }
    };
}
