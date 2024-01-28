package cn.hackedmc.alexander.module.impl.render.fullbright;

import cn.hackedmc.alexander.module.impl.render.FullBright;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Strikeless
 * @since 04.11.2021
 */
public final class GammaFullBright extends Mode<FullBright> {

    private float oldGamma;

    public GammaFullBright(String name, FullBright parent) {
        super(name, parent);
    }


    @EventLink()
    public final Listener<TickEvent> onTick = event -> {
        mc.gameSettings.gammaSetting = 100.0F;
    };

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}