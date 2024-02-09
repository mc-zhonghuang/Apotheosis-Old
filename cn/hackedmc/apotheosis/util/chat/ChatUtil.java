package cn.hackedmc.apotheosis.util.chat;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.render.Interface;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.localization.Localization;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import lombok.experimental.UtilityClass;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

/**
 * This is a chat util which can be used to do various things related to chat
 *
 * @author Auth
 * @since 20/10/2021
 */
@UtilityClass
public class ChatUtil implements InstanceAccess {

    /**
     * Adds a message to the players chat without sending it to the server
     *
     * @param message message that is going to be added to chat
     */
    public void display(final Object message, final Object... objects) {
        if (mc.thePlayer != null) {
            final String format = String.format(Localization.get(message.toString()), objects);

            mc.thePlayer.addChatMessage(new ChatComponentText(getPrefix() + format));
        }
    }

    public void displayIRC(final Object message, final Object... objects) {
        if (!Interface.INSTANCE.isEnabled() || !Interface.INSTANCE.irc.getValue()) return;

        final String format = String.format(message.toString(), objects);

        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§l[§bIRC§r§l] §r" + format));
        } else {
            System.out.println(format);
        }
    }

    public void displayNoPrefix(final Object message, final Object... objects) {
        final String format = String.format(message.toString(), objects);

        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(format));
        } else {
            System.out.println(format);
        }
    }

    /**
     * Sends a message in the chat
     *
     * @param message message that is going to be sent in chat
     */
    public void send(final Object message) {
        if (mc.thePlayer != null) {
            PacketUtil.send(new C01PacketChatMessage(message.toString()));
        }
    }

    private String getPrefix() {
        final String color = Client.INSTANCE.getThemeManager().getTheme().getChatAccentColor().toString();
        return EnumChatFormatting.BOLD + color + Client.NAME
                + EnumChatFormatting.RESET + color + " » "
                + EnumChatFormatting.RESET;
    }
}
