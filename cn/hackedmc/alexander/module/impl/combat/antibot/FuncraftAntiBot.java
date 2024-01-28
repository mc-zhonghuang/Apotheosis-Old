package cn.hackedmc.alexander.module.impl.combat.antibot;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.module.impl.combat.AntiBot;
import cn.hackedmc.alexander.value.Mode;

/**
 * @author Wykt
 * @since 2/04/2023
 */

public final class FuncraftAntiBot extends Mode<AntiBot> {
    public FuncraftAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @EventLink
    private final Listener<PreUpdateEvent> preUpdateEventListener = event -> {
        mc.theWorld.playerEntities.forEach(player -> {
            if(player.getDisplayName().getUnformattedText().contains("§")) {
                Client.INSTANCE.getBotManager().remove(player);
                return;
            }

            Client.INSTANCE.getBotManager().add(player);
        });
    };
}