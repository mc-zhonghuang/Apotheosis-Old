package cn.hackedmc.alexander.module.impl.render.fullbright;

import cn.hackedmc.alexander.module.impl.render.FullBright;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Strikeless (mode), Patrick (implementation)
 * @since 04.11.2021
 */
public final class EffectFullBright extends Mode<FullBright> {

    public EffectFullBright(String name, FullBright parent) {
        super(name, parent);
    }


    @EventLink()
    public final Listener<TickEvent> onTick = event -> {
        mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, Integer.MAX_VALUE, 1));
    };

    @Override
    public void onDisable() {
        if (mc.thePlayer.isPotionActive(Potion.nightVision)) {
            mc.thePlayer.removePotionEffect(Potion.nightVision.id);
        }
    }
}