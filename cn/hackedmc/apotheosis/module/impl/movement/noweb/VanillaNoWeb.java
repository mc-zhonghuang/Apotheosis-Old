package cn.hackedmc.apotheosis.module.impl.movement.noweb;

import cn.hackedmc.apotheosis.module.impl.movement.NoWeb;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.value.Mode;

public class VanillaNoWeb extends Mode<NoWeb> {
    public VanillaNoWeb(String name, NoWeb parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = event -> mc.thePlayer.isInWeb = false;
}