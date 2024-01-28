package cn.hackedmc.alexander.module.impl.ghost.wtap;


import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.module.impl.ghost.WTap;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.AttackEvent;
import cn.hackedmc.alexander.value.Mode;

public class LegitWTap extends Mode<WTap> {

    private boolean unsprint, wTap;

    public LegitWTap(String name, WTap parent) {
        super(name, parent);
    }


    @EventLink()
    public final Listener<AttackEvent> onAttack = event -> {
        wTap = Math.random() * 100 < getParent().chance.getValue().doubleValue();

        if (!wTap) return;

        if (mc.thePlayer.isSprinting() || mc.gameSettings.keyBindSprint.isKeyDown()) {
            mc.gameSettings.keyBindSprint.setPressed(true);
            unsprint = true;
        }
    };

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (!wTap) return;

        if (unsprint) {
            mc.gameSettings.keyBindSprint.setPressed(false);
            unsprint = false;
        }
    };
}