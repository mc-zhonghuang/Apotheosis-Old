package cn.hackedmc.apotheosis.module.impl.player.antivoid;


import cn.hackedmc.apotheosis.module.impl.player.AntiVoid;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;

public class PositionAntiVoid extends Mode<AntiVoid>  {

    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);

    public PositionAntiVoid(String name, AntiVoid parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (mc.thePlayer.fallDistance > distance.getValue().floatValue() && !PlayerUtil.isBlockUnder()) {
            event.setPosY(event.getPosY() + mc.thePlayer.fallDistance);
        }
    };
}