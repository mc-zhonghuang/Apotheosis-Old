package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.module.impl.combat.antibot.hyt.Basic;
import com.alan.clients.module.impl.combat.antibot.hyt.KitBattle;
import com.alan.clients.module.impl.combat.antibot.hyt.XP;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.ModeValue;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class HuaYuTingAntiBot extends Mode<AntiBot> {
    public HuaYuTingAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new Basic("Basic", this))
            .add(new XP("XP", this))
            .add(new KitBattle("Kit Battle", this))
            .setDefault("Basic");

    @Override
    public void onEnable() {
        this.bots.clear();
    }

    private final HashMap<Entity, Long> bots = new HashMap<>();

    public void becomeBotForATime(Entity entity, long time) {
        bots.put(entity, System.currentTimeMillis() + time);

        Client.INSTANCE.getBotManager().add(entity);
    }

    @EventLink
    private final Listener<TickEvent> onTick = event -> {
        for (Map.Entry<Entity, Long> entry : bots.entrySet()) if (entry.getValue() >= System.currentTimeMillis()) Client.INSTANCE.getBotManager().remove(entry.getKey()); // Check player
    };

    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = event -> {
        this.bots.clear();
    };
}
