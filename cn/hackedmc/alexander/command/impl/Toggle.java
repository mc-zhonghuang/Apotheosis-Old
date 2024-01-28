package cn.hackedmc.alexander.command.impl;
import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.command.Command;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.util.localization.Localization;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author Patrick
 * @since 10/19/2021
 */
public final class Toggle extends Command {

    public Toggle() {
        super("command.toggle.description", "toggle", "t");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            error(String.format(".%s <module>", args[0]));
            return;
        }
        final Module module = Client.INSTANCE.getModuleManager().get(args[1]);
        if (module == null) {
            ChatUtil.display(Localization.get("command.bind.invalidmodule"));
            return;
        }
        module.toggle();
        ChatUtil.display(
                Localization.get("command.toggle.toggled"),
                Localization.get(module.getModuleInfo().name()) + " " + (module.isEnabled() ?  EnumChatFormatting.GREEN + "on": EnumChatFormatting.RED + "off")
        );
    }
}