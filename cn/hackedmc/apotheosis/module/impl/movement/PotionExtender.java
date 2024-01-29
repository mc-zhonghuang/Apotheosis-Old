package cn.hackedmc.apotheosis.module.impl.movement;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.potion.PotionEffect;
import util.time.StopWatch;

import java.util.HashMap;

/**
 * @author Auth
 * @since 23/07/2022
 */
@Rise
@ModuleInfo(name = "module.movement.potionextender.name", description = "module.movement.potionextender.description", category = Category.MOVEMENT)
public class PotionExtender extends Module {

    private final NumberValue extendDuration = new NumberValue("Extend Duration", this, 10, 1, 60, 1);

    public final HashMap<PotionEffect, StopWatch> potions = new HashMap<>();

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        for (final PotionEffect potion : mc.thePlayer.getActivePotionEffects()) {
            if (potions.containsKey(potion)) {
                final StopWatch stopWatch = potions.get(potion);
                if (stopWatch.finished(this.extendDuration.getValue().longValue() * 1000)) {
                    mc.thePlayer.removePotionEffect(potion.getPotionID());
                    potions.remove(potion);
                }
            } else if (potion.duration == 1) {
                final StopWatch stopWatch = new StopWatch();
                stopWatch.reset();
                potions.put(potion, stopWatch);
            }
        }
    };
}