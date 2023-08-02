package com.alan.clients.module.impl.combat.antibot.hyt;

import com.alan.clients.module.impl.combat.antibot.HuaYuTingAntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KitBattle extends Mode<HuaYuTingAntiBot> {
    public KitBattle(String name, HuaYuTingAntiBot parent) {
        super(name, parent);
    }

    private final NumberValue delay = new NumberValue("Player Return Delay", this, 4700, 4000, 10000, 200);

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String text = wrapper.getChatComponent().getUnformattedText();

            if (!(text.startsWith("起床战争") || text.startsWith("[起床战争") || text.startsWith("花雨庭")) ||(text.contains(":") && !text.contains(mc.thePlayer.getDisplayName().getUnformattedText() + ":") && text.contains("起床战争") && !text.contains("01:00:00 是这个地图的记录!") && !text.contains("之队队设置一个新的记录:"))) return;

            final Matcher bot1 = Pattern.compile("击杀了(.*?) !").matcher(text);
            final Matcher bot2 = Pattern.compile("花雨庭 >>(.*?) 被").matcher(text);
            if (bot1.find()) {
                final String playerName = bot1.group(1).trim();
                if (playerName.equals(mc.thePlayer.getDisplayName().getUnformattedText())) return;
                for (EntityPlayer player : mc.theWorld.playerEntities) {
                    if (player.getDisplayName().getUnformattedText().equals(playerName))
                        this.getParent().becomeBotForATime(player, delay.getValue().intValue());
                }
            }
            if (bot2.find()) {
                final String playerName = bot2.group(1).trim();
                if (playerName.equals(mc.thePlayer.getDisplayName().getUnformattedText())) return;
                for (EntityPlayer player : mc.theWorld.playerEntities) {
                    if (player.getDisplayName().getUnformattedText().equals(playerName))
                        this.getParent().becomeBotForATime(player, delay.getValue().intValue());
                }
            }
        }
    };
}
