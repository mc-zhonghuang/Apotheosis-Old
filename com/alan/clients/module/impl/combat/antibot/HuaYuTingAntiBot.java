package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.value.Mode;
import net.minecraft.entity.player.EntityPlayer;

public class HuaYuTingAntiBot extends Mode<AntiBot> {
    public HuaYuTingAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getBotManager().clear();
    }

    @EventLink
    private final Listener<TickEvent> onTick = event -> {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.isPlayerSleeping() && !Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else Client.INSTANCE.getBotManager().remove(player);
            if (player.ticksExisted <= 80 && !Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else Client.INSTANCE.getBotManager().remove(player);
        }
    };
}
