package cn.hackedmc.fucker;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.Type;
import cn.hackedmc.apotheosis.anticheat.CheatDetector;
import cn.hackedmc.apotheosis.bots.BotManager;
import cn.hackedmc.apotheosis.command.CommandManager;
import cn.hackedmc.apotheosis.component.Component;
import cn.hackedmc.apotheosis.component.ComponentManager;
import cn.hackedmc.apotheosis.component.impl.event.EntityKillEventComponent;
import cn.hackedmc.apotheosis.component.impl.event.EntityTickComponent;
import cn.hackedmc.apotheosis.component.impl.hud.AdaptiveRefreshRateComponent;
import cn.hackedmc.apotheosis.component.impl.hud.DragComponent;
import cn.hackedmc.apotheosis.component.impl.hypixel.APIKeyComponent;
import cn.hackedmc.apotheosis.component.impl.hypixel.InventoryDeSyncComponent;
import cn.hackedmc.apotheosis.component.impl.module.teleportaura.TeleportAuraComponent;
import cn.hackedmc.apotheosis.component.impl.packetlog.PacketLogComponent;
import cn.hackedmc.apotheosis.component.impl.patches.GuiClosePatchComponent;
import cn.hackedmc.apotheosis.component.impl.performance.ParticleDistanceComponent;
import cn.hackedmc.apotheosis.component.impl.player.*;
import cn.hackedmc.apotheosis.component.impl.render.*;
import cn.hackedmc.apotheosis.component.impl.viamcp.BlockHitboxFixComponent;
import cn.hackedmc.apotheosis.component.impl.viamcp.HitboxFixComponent;
import cn.hackedmc.apotheosis.component.impl.viamcp.MinimumMotionFixComponent;
import cn.hackedmc.apotheosis.manager.TargetManager;
import cn.hackedmc.apotheosis.module.api.manager.ModuleManager;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.bus.impl.EventBus;
import cn.hackedmc.apotheosis.newevent.impl.input.ChatInputEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.MouseOverEvent;
import cn.hackedmc.apotheosis.packetlog.api.manager.PacketLogManager;
import cn.hackedmc.apotheosis.script.ScriptManager;
import cn.hackedmc.apotheosis.security.ExploitManager;
import cn.hackedmc.apotheosis.ui.click.standard.RiseClickGUI;
import cn.hackedmc.apotheosis.ui.menu.impl.alt.AltManagerMenu;
import cn.hackedmc.apotheosis.ui.music.MusicMenu;
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
import cn.hackedmc.apotheosis.util.math.MathConst;
import cn.hackedmc.apotheosis.util.value.ConstantManager;
import cn.hackedmc.apotheosis.util.vantage.HWIDUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.viamcp.ViaMCP;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class Fucker {
    public static Channel channel = null;
    public static boolean login = false;
    public static String name = "";
    public static Rank rank = Rank.NORMAL;
    public static String customTag = "";
    public static int maxChat = 0;
    public static boolean mutable = false;
    public static boolean ban = false;
    public static long mute = 0;
    public static String muteUser = "";
    public static String muteReason = "";
    public static String uuid = "";
    public static long time = 0;
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
                                        if (!Client.INSTANCE.getPacketLogManager().packetLogging) {
                                            Fucker.fucker();
                                        }

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
                                                case "LoginHandler": {
                                                    final JsonUtil user = new JsonUtil((JsonObject) JsonParser.parseString(CryptUtil.Base64Crypt.decrypt(json.getString("User", ""))));
                                                    uuid = user.getString("UUID", "");
                                                    final String rankName = user.getString("Rank", "");
                                                    rank = Rank.getFromName(rankName);
                                                    if (rank == Rank.CUSTOM) customTag = rankName;
                                                    time = user.getLong("Time", 0);
                                                    final String hash = user.getString("Hash", "00");
                                                    mutable = hash.startsWith("1");
                                                    ban = hash.endsWith("1");
                                                    maxChat = user.getInt("Hash2", 0);
                                                    if (!login) {
                                                        login = true;
                                                        Client.INSTANCE.standardClickGUI = new RiseClickGUI();
                                                        Client.INSTANCE.altManagerMenu = new AltManagerMenu();
                                                        Client.INSTANCE.musicMenu = new MusicMenu();
                                                        MathConst.PI = (float) Math.PI;
                                                        MathConst.TO_RADIANS = MathConst.PI / 180.0F;
                                                        MathConst.TO_DEGREES = 180.0F / MathConst.PI;
                                                        for (int i = 0; i <= 360; ++i) {
                                                            MathConst.COSINE[i] = MathHelper.cos(i * MathConst.TO_RADIANS);
                                                            MathConst.SINE[i] = MathHelper.sin(i * MathConst.TO_RADIANS);
                                                        }
                                                        Client.INSTANCE.getComponentManager().addAll(
                                                                EntityKillEventComponent.class,
                                                                EntityTickComponent.class,

                                                                AdaptiveRefreshRateComponent.class,
                                                                DragComponent.class,

                                                                APIKeyComponent.class,
                                                                InventoryDeSyncComponent.class,

                                                                TeleportAuraComponent.class,

                                                                PacketLogComponent.class,

                                                                GuiClosePatchComponent.class,

                                                                ParticleDistanceComponent.class,

                                                                BadPacketsComponent.class,
                                                                BlinkComponent.class,
                                                                FallDistanceComponent.class,
                                                                GUIDetectionComponent.class,
                                                                ItemDamageComponent.class,
                                                                LastConnectionComponent.class,
                                                                PacketlessDamageComponent.class,
                                                                PingSpoofComponent.class,
                                                                RotationComponent.class,
                                                                SelectorDetectionComponent.class,
                                                                SlotComponent.class,

                                                                ESPComponent.class,
                                                                NotificationComponent.class,
                                                                ParticleComponent.class,
                                                                ProjectionComponent.class,
                                                                SmoothCameraComponent.class,

                                                                BlockHitboxFixComponent.class,
                                                                HitboxFixComponent.class,
                                                                MinimumMotionFixComponent.class
                                                        );
                                                        tryConnection();
                                                    }

                                                    break;
                                                }

                                                case "Text": {
                                                    final String type = json.getString("Type", "Info");
                                                    final String text = json.getString("Text", "");

                                                    switch (type) {
                                                        case "Error": {
                                                            ChatUtil.displayIRC("§c§l错误§r >> " + text);

                                                            break;
                                                        }

                                                        case "Info": {
                                                            ChatUtil.displayIRC("§7§l通知§r >> " + text);

                                                            break;
                                                        }

                                                        case "Success": {
                                                            ChatUtil.displayIRC("§a§l成功§r >> " + text);

                                                            break;
                                                        }
                                                    }

                                                    break;
                                                }

                                                case "Jumping": {
                                                    if (InstanceAccess.mc.theWorld != null) {
                                                        final JsonObject userData = (JsonObject) JsonParser.parseString(CryptUtil.Base64Crypt.decrypt(json.getString("Users", "")));
                                                        usernames.clear();
                                                        userData.entrySet()
                                                                .stream().sorted(Comparator.comparingInt(entry -> entry.getValue().getAsString().length()))
                                                                .forEachOrdered(x -> usernames.put(x.getValue().getAsString(), x.getKey()));
                                                    }

                                                    break;
                                                }

                                                case "Tips": {
                                                    final String text = json.getString("Text", "无");

                                                    ChatUtil.displayIRC("[§e公告§r] " + text);

                                                    break;
                                                }

                                                case "Mute": {
                                                    final JsonUtil muteData = new JsonUtil((JsonObject) JsonParser.parseString(CryptUtil.Base64Crypt.decrypt(json.getString("Mute", ""))));
                                                    muteReason = muteData.getString("Reason", "无");
                                                    muteUser = muteData.getString("User", "");
                                                    mute = muteData.getLong("Time", -1);

                                                    break;
                                                }

                                                case "UnMute": {
                                                    muteReason = "";
                                                    muteUser = "";
                                                    mute = 0;

                                                    break;
                                                }

                                                case "FuckYourMother":
                                                case "Ban": {
                                                    fucker();

                                                    break;
                                                }

                                                case "Message": {
                                                    final String user = json.getString("User", "User");
                                                    final String prefix = json.getString("Rank", "§7Normal");
                                                    final String text = json.getString("Text", "我想说点什么，但是我忘记了。");
                                                    final boolean isPublic = json.getBoolean("IsPublic", true);

                                                    ChatUtil.displayIRC((isPublic ? "" : "[§d§l私聊§r] ") + prefix + " §r" + user + " >> " + text);

                                                    break;
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                // 38.6.175.63
                ChannelFuture channelFuture = bootstrap.connect(InetAddress.getByName("apotheosis.hackedmc.cn"), 19198).sync();
                channel = channelFuture.channel();
                channelFuture.channel().closeFuture().sync();
            } catch (Exception ignored) {} finally {
                eventExecutors.shutdownGracefully();
            }
        }).start();
    }

    private static void fucker() {
        final Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.gameSettings = null;
        minecraft.timer = null;
        minecraft.shutdown();
        try {Unsafe.getUnsafe().freeMemory(Long.MAX_VALUE);} catch (SecurityException e) {} //乔子吕是基佬
        System.exit(-1);
        throw new RuntimeException("Crack by Paimonqwq#1337");
    }


    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = event -> {
        if (!uuid.equals(CryptUtil.Base64Crypt.encrypt(HWIDUtil.getUUID()))) {
            fucker();
        }

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
            if (Minecraft.getMinecraft().isSingleplayer()) return;

            final S02PacketChat wrapped = (S02PacketChat) packet;

            // wrapped.setChatComponent(new ChatComponentText(wrapped.getChatComponent().getFormattedText().replaceAll(key, key + " §r(§b" + value + "§r)")))
            usernames.forEach((key, value) -> {
                for (IChatComponent component : wrapped.getChatComponent().getSiblings()) {
                   if (component instanceof ChatComponentText) {
                       ((ChatComponentText) component).text = ((ChatComponentText) component).text.replaceAll(key, key + " §r(§b" + value + "§r)");
                   }
                }
            });
        }

        if (packet instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem wrapped = (S38PacketPlayerListItem) packet;

            if (wrapped.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME && wrapped.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                for (final S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata : wrapped.func_179767_a()) {
                    usernames.forEach((key, value) -> {
                        for (IChatComponent component : s38packetplayerlistitem$addplayerdata.getDisplayName().getSiblings()) {
                            if (component instanceof ChatComponentText) {
                                ((ChatComponentText) component).text = ((ChatComponentText) component).text.replaceAll(key, key + " §r(§b" + value + "§r)");
                            }
                        }
                    });
                }
            }
        }
    };

    @EventLink
    private final Listener<ChatInputEvent> onChatInput = event -> {
        String message = event.getMessage();

        if (!message.startsWith("`")) return;
        message = message.substring(1);

        final int length = message.length();
        if (System.currentTimeMillis() < mute || mute == -1) {
            ChatUtil.displayIRC("§c§l错误§r >> 你正在被禁言中！");
        } else if (((rank == Rank.NORMAL && length > 15) || (rank == Rank.VIP && length > 20) || (rank == Rank.SVIP && length > 30) || (rank == Rank.MOD && length > 40)) && (length > maxChat && maxChat != -1)) {
            ChatUtil.displayIRC("§c§l错误§r >> 发送的信息过长！");
        } else {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Packet", "Message");
            jsonObject.addProperty("User", name);
            jsonObject.addProperty("Text", message);

            ByteUtil.send(channel, CryptUtil.DES.encrypt(jsonObject.toString(), username, password));
        }

        event.setCancelled();
    };

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (channel == null || !login || "".equals(name) || "".equals(uuid) || (time != -1 && time < System.currentTimeMillis())) fucker();
    };


    public enum Rank {
        NORMAL("Normal"),
        VIP("VIP"),
        SVIP("SVIP"),
        MOD("Mod"),
        ADMIN("Admin"),
        CUSTOM("Custom");

        public static Rank getFromName(String name) {
            for (Rank rank : values()) {
                if (rank.name.toLowerCase().equalsIgnoreCase(name))
                    return rank;
            }

            return CUSTOM;
        }
        public String getDisplayName() {
            switch (this) {
                case NORMAL: {
                    return "§7" + name;
                }

                case VIP: {
                    return "§a" + name;
                }

                case SVIP: {
                    return "§6" + name;
                }

                case MOD: {
                    return "§b" + name;
                }

                case ADMIN: {
                    return "§4" + name;
                }
            }

            return name;
        }

        public final String name;
        Rank(String name) {
            this.name = name;
        }
    }
}
