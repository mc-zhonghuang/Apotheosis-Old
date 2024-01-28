package cn.hackedmc.alexander.command.impl;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.command.Command;

/**
 * @author Alan
 * @since 3/02/2022
 */
@Rise
public final class Panic extends Command {

    public Panic() {
        super("command.panic.description", "panic", "p");
    }

    @Override
    public void execute(final String[] args) {
        Client.INSTANCE.getModuleManager().getAll().stream().filter(module ->
                !module.getModuleInfo().autoEnabled()).forEach(module -> module.setEnabled(false));
    }
}