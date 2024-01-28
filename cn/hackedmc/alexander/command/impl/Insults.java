package cn.hackedmc.alexander.command.impl;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.command.Command;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;

@Rise
public final class Insults extends Command {

    public Insults() {
        super("command.insults.description", "insults", "killinsults", "insult");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length >= 3) {
            final String request = args[1].toLowerCase();
            final String name = args[2];

            if (request.equals("create")) {
                InstanceAccess.instance.getInsultManager().set(name);

                ChatUtil.display("command.insults.created", name);
            } else if (request.equals("delete")) {
                InstanceAccess.instance.getInsultManager().delete(name);

                ChatUtil.display("command.insults.removed", name);
            }
        } else {
            ChatUtil.display("command.insults.help1");
            ChatUtil.display("command.insults.help2");
        }
    }
}
