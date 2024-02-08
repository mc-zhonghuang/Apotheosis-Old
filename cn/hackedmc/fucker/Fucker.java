package cn.hackedmc.fucker;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.Type;
import cn.hackedmc.apotheosis.anticheat.CheatDetector;
import cn.hackedmc.apotheosis.bots.BotManager;
import cn.hackedmc.apotheosis.command.CommandManager;
import cn.hackedmc.apotheosis.component.ComponentManager;
import cn.hackedmc.apotheosis.manager.TargetManager;
import cn.hackedmc.apotheosis.module.api.manager.ModuleManager;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.bus.impl.EventBus;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.packetlog.api.manager.PacketLogManager;
import cn.hackedmc.apotheosis.script.ScriptManager;
import cn.hackedmc.apotheosis.security.ExploitManager;
import cn.hackedmc.apotheosis.ui.theme.ThemeManager;
import cn.hackedmc.apotheosis.util.ByteUtil;
import cn.hackedmc.apotheosis.util.CryptUtil;
import cn.hackedmc.apotheosis.util.JsonUtil;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
import cn.hackedmc.apotheosis.util.file.FileManager;
import cn.hackedmc.apotheosis.util.file.alt.AltManager;
import cn.hackedmc.apotheosis.util.file.config.ConfigManager;
import cn.hackedmc.apotheosis.util.file.data.DataManager;
import cn.hackedmc.apotheosis.util.file.insult.InsultManager;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.localization.Locale;
import cn.hackedmc.apotheosis.util.value.ConstantManager;
import cn.hackedmc.apotheosis.util.vantage.HWIDUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.util.ChatComponentText;
import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fucker {
    public static Channel channel = null;
    public static boolean login = false;
    public static String name = "";
    public static final String username = "5oiR5piv56eR5q+U5oiR5p2A5q275LqG55u05Y2H6aOe5py6";
    public static final byte[] password = new byte[]{1, 9, 8, 9, 0, 6, 0, 4};
    public static final LinkedHashMap<String, String> usernames = new LinkedHashMap<>();

    public static void fuckClass(Class<?> clazz, Object instance) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                final Class<?> type = field.getType();

                final boolean canVisit = field.isAccessible();
                if (!canVisit)
                    field.setAccessible(true);

                if (type == Locale.class) {
                    field.set(instance, Locale.EN_US);
                }

                if (type == Type.class) {
                    field.set(instance, Type.BASIC);
                }

                if (type == ModuleManager.class) {
                    field.set(instance, new ModuleManager());
                }

                if (type == ComponentManager.class) {
                    field.set(instance, new ComponentManager());
                }

                if (type == CommandManager.class) {
                    field.set(instance, new CommandManager());
                }

                if (type == FileManager.class) {
                    field.set(instance, new FileManager());
                }

                if (type == ConfigManager.class) {
                    field.set(instance, new ConfigManager());
                }

                if (type == AltManager.class) {
                    field.set(instance, new AltManager());
                }

                if (type == InsultManager.class) {
                    field.set(instance, new InsultManager());
                }

                if (type == DataManager.class) {
                    field.set(instance, new DataManager());
                }

                if (type == ExploitManager.class) {
                    field.set(instance, new ExploitManager());
                }

                if (type == BotManager.class) {
                    field.set(instance, new BotManager());
                }

                if (type == ThemeManager.class) {
                    field.set(instance, new ThemeManager());
                }

                if (type == ScriptManager.class) {
                    field.set(instance, new ScriptManager());
                }

                if (type == TargetManager.class) {
                    field.set(instance, new TargetManager());
                }

                if (type == CheatDetector.class) {
                    field.set(instance, new CheatDetector());
                }

                if (type == ConstantManager.class) {
                    field.set(instance, new ConstantManager());
                }

                if (type == EventBus.class) {
                    field.set(instance, new EventBus<>());
                }

                if (type == PacketLogManager.class) {
                    field.set(instance, new PacketLogManager());
                }

                if (!canVisit) {
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            fucker();
        }

        Client.INSTANCE.getEventBus().register(new Fucker());
    }

    public static void tryConnection() {
        new Thread(() -> {
            while (true) {
                if (channel != null) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Packet", "Jumping");
                    jsonObject.addProperty("User", name);
                    jsonObject.addProperty("GameId", InstanceAccess.mc.thePlayer == null ? InstanceAccess.mc.session.getUsername() : InstanceAccess.mc.thePlayer.getCommandSenderName());

                    ByteUtil.send(channel, CryptUtil.DES.encrypt(jsonObject.toString(), username, password));
                }
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void fuckThis() {
        new Thread(() -> {
            NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventExecutors)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        final ByteBuf byteBuf = (ByteBuf) msg;
                                        final byte[] bytes = new byte[byteBuf.readableBytes()];
                                        byteBuf.readBytes(bytes);
                                        final String wrapped = CryptUtil.DES.decrypt(bytes, username, password);
                                        if (wrapped.equals("")) return;
                                        final JsonObject jsonObject = (JsonObject) JsonParser.parseString(wrapped);

                                        if (jsonObject.has("Packet")) {
                                            final String packetId = jsonObject.get("Packet").getAsString();
                                            final JsonUtil json = new JsonUtil(jsonObject);

                                            switch (packetId) {
                                                case "Text": {
                                                    final String type = json.getString("Type", "Info");
                                                    final String text = json.getString("Text", "");

                                                    switch (type) {
                                                        case "Error": {
                                                            ChatUtil.displayNoPrefix("警告 >> " + text);

                                                            break;
                                                        }

                                                        case "Info": {
                                                            ChatUtil.displayNoPrefix("通知 >> " + text);

                                                            break;
                                                        }

                                                        case "Success": {
                                                            if (login) {
                                                                ChatUtil.displayNoPrefix("成功 >> " + text);
                                                            } else {
                                                                login = true;
                                                                tryConnection();
                                                            }

                                                            break;
                                                        }
                                                    }

                                                    break;
                                                }

                                                case "Jumping": {
                                                    if (InstanceAccess.mc.theWorld != null) {
                                                        final JsonObject userData = (JsonObject) JsonParser.parseString(CryptUtil.Base64Crypt.decrypt(json.getString("Users", "")));
                                                        userData.entrySet().forEach(entry -> usernames.put(entry.getValue().getAsString(), entry.getKey()));

                                                        userData.entrySet()
                                                                .stream().sorted(Comparator.comparingInt(entry -> entry.getValue().getAsString().length()))
                                                                .forEachOrdered(x -> usernames.put(x.getValue().getAsString(), x.getKey()));
                                                    }

                                                    break;
                                                }

                                                case "FuckYourMother": {
                                                    fucker();

                                                    break;
                                                }

                                                case "Ban": {
                                                    final String uuid = json.getString("UUID", "");

                                                    if (uuid.equals(HWIDUtil.lastHWID))
                                                        fucker();

                                                    break;
                                                }

                                                case "Message": {
                                                    final String user = json.getString("User", "User");
                                                    final String prefix = json.getString("Rank", "§7Normal");
                                                    final String text = json.getString("Text", "我想说点什么，但是我忘记了。");

                                                    ChatUtil.displayNoPrefix(prefix + " §r" + user + " >> " + text);

                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect("38.6.175.63", 19198).sync();
                channel = channelFuture.channel();
                channelFuture.channel().closeFuture().sync();
            } catch (Exception ignored) {} finally {
                eventExecutors.shutdownGracefully();
            }
        }).start();
    }

    private static void fucker() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.shutdown();
        minecraft.gameSettings = null;
        minecraft.timer = null;
        Unsafe.getUnsafe().freeMemory(Long.MAX_VALUE);
        System.exit(-1);
        throw new RuntimeException("Crack by Paimonqwq#1337");
    }

    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = event -> {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Packet", "Jumping");
        jsonObject.addProperty("User", name);
        jsonObject.addProperty("GameId", InstanceAccess.mc.thePlayer == null ? InstanceAccess.mc.session.getUsername() : InstanceAccess.mc.thePlayer.getCommandSenderName());

        ByteUtil.send(channel, CryptUtil.DES.encrypt(jsonObject.toString(), username, password));
    };

    @EventLink
    private Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapped = (S02PacketChat) packet;

            usernames.forEach((key, value) -> wrapped.setChatComponent(new ChatComponentText(wrapped.getChatComponent().getFormattedText().replaceAll(key, key + " §r(§b" + value + "§r)"))));
        }

        if (packet instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem wrapped = (S38PacketPlayerListItem) packet;

            if (wrapped.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME && wrapped.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                for (final S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : wrapped.func_179767_a()) {
                    usernames.forEach((key, value) -> s38packetplayerlistitem$addplayerdata.setDisplayName(new ChatComponentText(s38packetplayerlistitem$addplayerdata.getDisplayName().getFormattedText().replaceAll(key, key + " §r(§b" + value + "§r)"))));
                }
            }
        }
    };
}
