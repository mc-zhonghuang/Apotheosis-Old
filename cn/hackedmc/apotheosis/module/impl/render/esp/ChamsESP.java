package cn.hackedmc.apotheosis.module.impl.render.esp;

import cn.hackedmc.apotheosis.component.impl.render.ESPComponent;
import cn.hackedmc.apotheosis.component.impl.render.espcomponent.api.ESPColor;
import cn.hackedmc.apotheosis.component.impl.render.espcomponent.impl.PlayerChams;
import cn.hackedmc.apotheosis.module.impl.render.ESP;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.value.Mode;

import java.awt.*;

public final class ChamsESP extends Mode<ESP> {

    public ChamsESP(String name, ESP parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        Color color = getTheme().getFirstColor();
        ESPComponent.add(new PlayerChams(new ESPColor(color, color, color)));
    };
}