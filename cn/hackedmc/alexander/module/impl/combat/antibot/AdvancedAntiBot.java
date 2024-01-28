package cn.hackedmc.alexander.module.impl.combat.antibot;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.impl.combat.AntiBot;
import cn.hackedmc.alexander.value.Mode;

public final class AdvancedAntiBot extends Mode<AntiBot> {

    public AdvancedAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        mc.theWorld.playerEntities.forEach(player -> {
            if (mc.thePlayer.getDistanceSq(player.posX, mc.thePlayer.posY, player.posZ) > 200) {
                Client.INSTANCE.getBotManager().remove(player);
            }

            if (player.ticksExisted < 5 || player.isInvisible() || mc.thePlayer.getDistanceSq(player.posX, mc.thePlayer.posY, player.posZ) > 100 * 100) {
                Client.INSTANCE.getBotManager().add(player);
            }
        });
    };

}