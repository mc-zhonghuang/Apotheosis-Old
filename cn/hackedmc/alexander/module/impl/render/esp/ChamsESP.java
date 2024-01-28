package cn.hackedmc.alexander.module.impl.render.esp;

import cn.hackedmc.alexander.component.impl.render.ESPComponent;
import cn.hackedmc.alexander.component.impl.render.espcomponent.api.ESPColor;
import cn.hackedmc.alexander.component.impl.render.espcomponent.impl.PlayerChams;
import cn.hackedmc.alexander.module.impl.render.ESP;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.value.Mode;

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