package cn.hackedmc.apotheosis.module.impl.render.esp;

import cn.hackedmc.apotheosis.component.impl.render.ESPComponent;
import cn.hackedmc.apotheosis.component.impl.render.espcomponent.api.ESPColor;
import cn.hackedmc.apotheosis.component.impl.render.espcomponent.impl.PlayerGlow;
import cn.hackedmc.apotheosis.module.impl.render.ESP;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.value.Mode;

import java.awt.*;

public final class GlowESP extends Mode<ESP> {

    public GlowESP(String name, ESP parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        Color color = getTheme().getFirstColor();
        ESPComponent.add(new PlayerGlow(new ESPColor(color, color, color)));
    };
}