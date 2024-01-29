package cn.hackedmc.apotheosis.module.impl.combat.antibot;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.TickEvent;
import cn.hackedmc.apotheosis.module.impl.combat.AntiBot;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import net.minecraft.entity.player.EntityPlayer;

public class HuaYuTingAntiBot extends Mode<AntiBot> {
    public HuaYuTingAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    private final BooleanValue armor = new BooleanValue("Armor Check", this, true);

    @Override
    public void onEnable() {
        Client.INSTANCE.getBotManager().clear();
    }

    @EventLink
    private final Listener<TickEvent> onTick = event -> {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.isPlayerSleeping()) if (!Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else if (player.ticksExisted <= 80) if (!Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else if (player.getEyeHeight() <= 0.5f) if (!Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else if (armor.getValue() && mc.thePlayer.inventory.armorInventory[3] == null) if (!Client.INSTANCE.getBotManager().contains(player)) Client.INSTANCE.getBotManager().add(player);
            else Client.INSTANCE.getBotManager().remove(player);
        }
    };
}
