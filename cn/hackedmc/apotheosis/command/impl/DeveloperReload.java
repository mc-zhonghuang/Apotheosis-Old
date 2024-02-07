package cn.hackedmc.apotheosis.command.impl;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.command.Command;
import cn.hackedmc.apotheosis.module.api.DevelopmentFeature;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;

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
        Client.INSTANCE.initClient();
        ChatUtil.display("Reloaded Apotheosis");
    }
}