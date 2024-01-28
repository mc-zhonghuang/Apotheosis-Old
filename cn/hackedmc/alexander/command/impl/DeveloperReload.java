package cn.hackedmc.alexander.command.impl;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.command.Command;
import cn.hackedmc.alexander.module.api.DevelopmentFeature;
import cn.hackedmc.alexander.util.chat.ChatUtil;

/**
 * @author Alan
 * @since 10/19/2021
 */
@Rise
@DevelopmentFeature
public final class DeveloperReload extends Command {

    public DeveloperReload() {
        super("Reloads the client", "developerreload", "dr");
    }

    @Override
    public void execute(final String[] args) {
        Client.INSTANCE.terminate();
        Client.INSTANCE.initRise();
        ChatUtil.display("Reloaded Rise");
    }
}