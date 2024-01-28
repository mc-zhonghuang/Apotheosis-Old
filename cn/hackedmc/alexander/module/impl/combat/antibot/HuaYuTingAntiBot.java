package cn.hackedmc.alexander.module.impl.combat.antibot;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.module.impl.combat.AntiBot;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.BooleanValue;
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
