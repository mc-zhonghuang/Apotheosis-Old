package cn.hackedmc.apotheosis.command.impl;

import cn.hackedmc.apotheosis.command.Command;
import cn.hackedmc.apotheosis.util.ByteUtil;
import cn.hackedmc.apotheosis.util.CryptUtil;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
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
            error(".irc <online/message/chat/info" + (Fucker.rank.equals(Fucker.Rank.ADMIN) || Fucker.ban ? "/ban" : "") + (Fucker.rank.equals(Fucker.Rank.ADMIN) || Fucker.mutable ? "/mute/unmute" : "") + (Fucker.rank.equals(Fucker.Rank.ADMIN) || (Fucker.ban && Fucker.mutable) ? "/tips" : "") + "> (VALUE)");
        } else {
            if (args[1].equalsIgnoreCase("info")) {
                ChatUtil.displayNoPrefix("·======§b§lIRC Info§r======·");
                ChatUtil.displayNoPrefix("§bUsername§r -> " + Fucker.name);
                ChatUtil.displayNoPrefix("§bRank§r -> " + (Fucker.rank == Fucker.Rank.CUSTOM ? Fucker.customTag : Fucker.rank.getDisplayName()));
                ChatUtil.displayNoPrefix("§bTime§r -> " + (Fucker.time == -1 ? "Infinite" : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(Fucker.time))));
                ChatUtil.displayNoPrefix("§bMute§r -> " + (System.currentTimeMillis() < Fucker.mute || Fucker.mute == -1 ? "禁言中，来自" + Fucker.muteUser + "，原因:" + Fucker.muteReason + "，时长:" + (Fucker.mute == -1 ? "无限时长" : new SimpleDateFormat("yyyy年MM月dd天 HH小时mm分钟").format(new Date(Fucker.mute - System.currentTimeMillis()))) : "无"));
                ChatUtil.displayNoPrefix("·===================·");
            } else if (args[1].equalsIgnoreCase("Online")) {
                ChatUtil.displayNoPrefix("·======§b§lIRC Online§r======·");
                Fucker.usernames.forEach((key, value) -> {
                    ChatUtil.displayNoPrefix("§bUsername§r -> " + value + " §dGameId§r -> " + key);
                });
                ChatUtil.displayNoPrefix("·===================·");
            } else if (((args[1].equalsIgnoreCase("ban") || args[1].equalsIgnoreCase("chat") || args[1].equalsIgnoreCase("mute") || args[1].equalsIgnoreCase("unmute") || args[1].equalsIgnoreCase("tips")) && args.length > 2) || (args[1].equalsIgnoreCase("message") && args.length > 3)) {
                if (args[1].equalsIgnoreCase("message")) {
                    final String message = StringUtils.join(Arrays.stream(args).skip(3).collect(Collectors.toList()), " ");
                    final int length = message.length();
                    if (System.currentTimeMillis() < Fucker.mute || Fucker.mute == -1) {
                        ChatUtil.displayIRC("§c§l错误§r >> 你正在被禁言中！");
                    } else if (((Fucker.rank == Fucker.Rank.NORMAL && length > 15) || (Fucker.rank == Fucker.Rank.VIP && length > 20) || (Fucker.rank == Fucker.Rank.SVIP && length > 30) || (Fucker.rank == Fucker.Rank.MOD && length > 40)) && (length > Fucker.maxChat && Fucker.maxChat != -1)) {
                        ChatUtil.displayIRC("§c§l错误§r >> 发送的信息过长！");
                    } else {
                        final JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Packet", "Message");
                        jsonObject.addProperty("User", Fucker.name);
                        jsonObject.addProperty("ToUser", args[2]);
                        jsonObject.addProperty("Text", message);

                        ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                    }
                } else if (args[1].equalsIgnoreCase("tips")) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "Tips");
                    jsonObject.addProperty("User", Fucker.name);
                    jsonObject.addProperty("Text", StringUtils.join(Arrays.stream(args).skip(2).collect(Collectors.toList()), " "));

                    ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                } else if (args[1].equalsIgnoreCase("mute")) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "Mute");
                    jsonObject.addProperty("User", Fucker.name);
                    jsonObject.addProperty("MuteName", args[2]);
                    jsonObject.addProperty("Reason", args.length > 3 ? args[3] : "无");
                    jsonObject.addProperty("Time", args.length > 4 ? Long.parseLong(args[4]) * 1000 : -1);

                    ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                } else if (args[1].equalsIgnoreCase("unmute")) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "UnMute");
                    jsonObject.addProperty("User", Fucker.name);
                    jsonObject.addProperty("MuteName", args[2]);

                    ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                } else if (args[1].equalsIgnoreCase("ban")) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "Ban");
                    jsonObject.addProperty("User", Fucker.name);
                    jsonObject.addProperty("BanName", args[2]);

                    ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(jsonObject.toString(), Fucker.username, Fucker.password));
                } else {
                    final String message = StringUtils.join(Arrays.stream(args).skip(2).collect(Collectors.toList()), " ");
                    final int length = message.length();
                    if (System.currentTimeMillis() < Fucker.mute || Fucker.mute == -1) {
                        ChatUtil.displayIRC("§c§l错误§r >> 你正在被禁言中！");
                    } else if (((Fucker.rank == Fucker.Rank.NORMAL && length > 15) || (Fucker.rank == Fucker.Rank.VIP && length > 20) || (Fucker.rank == Fucker.Rank.SVIP && length > 30) || (Fucker.rank == Fucker.Rank.MOD && length > 40)) && (length > Fucker.maxChat && Fucker.maxChat != -1)) {
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
                error(".irc <online/message/chat/info" + (Fucker.rank.equals(Fucker.Rank.ADMIN) || Fucker.ban ? "/ban" : "") + (Fucker.rank.equals(Fucker.Rank.ADMIN) || Fucker.mutable ? "/mute/unmute" : "") + (Fucker.rank.equals(Fucker.Rank.ADMIN) || (Fucker.ban && Fucker.mutable) ? "/tips" : "") + "> (VALUE)");
            }
        }
    }
}
