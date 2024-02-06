package cn.hackedmc.apotheosis.module.impl.combat.criticals;

import cn.hackedmc.apotheosis.module.impl.combat.Criticals;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.AttackEvent;
import cn.hackedmc.apotheosis.value.Mode;

public class HopCriticals extends Mode<Criticals> {
    public HopCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    @EventLink
    private final Listener<AttackEvent> onAttack = event -> {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.08;
        }
    };
}
