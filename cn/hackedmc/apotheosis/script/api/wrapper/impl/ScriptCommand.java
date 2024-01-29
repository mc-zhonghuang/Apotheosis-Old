package cn.hackedmc.apotheosis.script.api.wrapper.impl;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.command.Command;
import cn.hackedmc.apotheosis.script.api.wrapper.ScriptHandlerWrapper;

/**
 * @author Strikeless
 * @since 15.05.2022
 */
public final class ScriptCommand extends ScriptHandlerWrapper<Command> {

    public ScriptCommand(final Command wrapped) {
        super(wrapped);
    }

    public void unregister() {
        Client.INSTANCE.getCommandManager().remove(this.wrapped);
    }

    // TODO: Make command execution again

    public String getName() {
        return this.wrapped.getExpressions()[0];
    }

    public String getDescription() {
        return this.wrapped.getDescription();
    }
}
