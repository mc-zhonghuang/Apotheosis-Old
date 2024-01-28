package cn.hackedmc.alexander.module.impl.combat.antibot;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.impl.combat.AntiBot;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.client.network.NetworkPlayerInfo;

public final class WatchdogAntiBot extends Mode<AntiBot> {

    public WatchdogAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        mc.theWorld.playerEntities.forEach(player -> {
            final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());

            if (info == null) {
                Client.INSTANCE.getBotManager().add(player);
            } else {
                Client.INSTANCE.getBotManager().remove(player);
            }
        });
    };
}