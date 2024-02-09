package cn.hackedmc.apotheosis.command.impl;

import cn.hackedmc.apotheosis.command.Command;
import cn.hackedmc.apotheosis.util.ByteUtil;
import cn.hackedmc.apotheosis.util.CryptUtil;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.fucker.Fucker;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public final class IRC extends Command {
    public IRC() {
        super("command.irc.description", "irc", "i");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            error(".irc <chat/info" + (Fucker.rank.equals(Fucker.Rank.ADMIN) ? "/ban" : "") + "> (VALUE)");
        } else {
            if (args[1].equalsIgnoreCase("info")) {
                ChatUtil.displayNoPrefix("§bUsername§r -> " + Fucker.name);
                ChatUtil.displayNoPrefix("§bRank§r -> " + Fucker.rank.getDisplayName());
                ChatUtil.displayNoPrefix("§bTime§r -> " + (Fucker.time == -1 ? "Infinite" : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Fucker.time))));
            } else if ((args[1].equalsIgnoreCase("ban") || args[1].equalsIgnoreCase("chat")) && args.length > 2) {
                if (args[1].equalsIgnoreCase("ban")) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "Ban");
                    jsonObject.addProperty("User", Fucker.name);
                    jsonObject.addProperty("BanName", args[2]);

                    ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                } else {
                    final String message = StringUtils.join(Arrays.stream(args).skip(2).collect(Collectors.toList()), " ");
                    final int length = message.length();
                    if ((Fucker.rank == Fucker.Rank.NORMAL && length > 15) || (Fucker.rank == Fucker.Rank.VIP && length > 20) || (Fucker.rank == Fucker.Rank.SVIP && length > 30) || (Fucker.rank == Fucker.Rank.MOD && length > 40)) {
                        ChatUtil.displayIRC("§c§l错误§r >> 发送的信息过长！");
                    } else {
                        final JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Packet", "Message");
                        jsonObject.addProperty("User", Fucker.name);
                        jsonObject.addProperty("Text", message);

                        ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                    }
                }
            } else {
                error(".irc <chat/info" + (Fucker.rank.equals(Fucker.Rank.ADMIN) ? "/ban" : "") + "> (VALUE)");
            }
        }
    }
}
