package cn.hackedmc.alexander.command.impl;

import cn.hackedmc.alexander.command.Command;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Patrick
 * @since 10/19/2021
 */
public final class Name extends Command {

    public Name() {
        super("command.name.description", "name", "ign", "username", "nick", "nickname");
    }

    @Override
    public void execute(final String[] args) {
        final String name = PlayerUtil.name();

        GuiScreen.setClipboardString(name);
        ChatUtil.display("command.name.copied", name);
    }
}
