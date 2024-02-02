package cn.hackedmc.apotheosis.module.impl.other.protocols;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.other.ServerProtocol;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.util.RandomUtil;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
import cn.hackedmc.apotheosis.value.Mode;
import com.google.common.base.Joiner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class HuaYuTingProtocol extends Mode<ServerProtocol> {
    public HuaYuTingProtocol(String name, ServerProtocol parent) {
        super(name, parent);

        buttons.put("起床练习",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 48, 34, 123, 34, 101, 110, 116, 114, 121, 34, 58, 48, 44, 34, 115, 105, 100, 34, 58, 34, 66, 69, 68, 87, 65, 82, 47, 98, 119, 45, 100, 97, 108, 117, 34, 125});
        buttons.put("起床单人",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 49, 34, 123, 34, 101, 110, 116, 114, 121, 34, 58, 49, 44, 34, 115, 105, 100, 34, 58, 34, 66, 69, 68, 87, 65, 82, 47, 98, 119, 45, 115, 111, 108, 111, 34, 125});
        buttons.put("起床双人",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 50, 36, 123, 34, 101, 110, 116, 114, 121, 34, 58, 50, 44, 34, 115, 105, 100, 34, 58, 34, 66, 69, 68, 87, 65, 82, 47, 98, 119, 45, 100, 111, 117, 98, 108, 101, 34, 125});
        buttons.put("起床四人",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 51, 34, 123, 34, 101, 110, 116, 114, 121, 34, 58, 51, 44, 34, 115, 105, 100, 34, 58, 34, 66, 69, 68, 87, 65, 82, 47, 98, 119, 45, 116, 101, 97, 109, 34, 125});
        buttons.put("空岛单人",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 48, 34, 123, 34, 101, 110, 116, 114, 121, 34, 58, 48, 44, 34, 115, 105, 100, 34, 58, 34, 83, 75, 89, 87, 65, 82, 47, 110, 115, 107, 121, 119, 97, 114, 34, 125});
        buttons.put("空岛双人",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 49, 41, 123, 34, 101, 110, 116, 114, 121, 34, 58, 49, 44, 34, 115, 105, 100, 34, 58, 34, 83, 75, 89, 87, 65, 82, 47, 110, 115, 107, 121, 119, 97, 114, 45, 100, 111, 117, 98, 108, 101, 34, 125});
        buttons.put("职业战争",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 50, 33, 123, 34, 101, 110, 116, 114, 121, 34, 58, 50, 44, 34, 115, 105, 100, 34, 58, 34, 70, 73, 71, 72, 84, 47, 107, 98, 45, 103, 97, 109, 101, 34, 125});
        buttons.put("天坑",new byte[]{0, 0, 0, 26, 20, 71, 85, 73, 36, 109, 97, 105, 110, 109, 101, 110, 117, 64, 101, 110, 116, 114, 121, 47, 52, 33, 123, 34, 101, 110, 116, 114, 121, 34, 58, 52, 44, 34, 115, 105, 100, 34, 58, 34, 70, 73, 71, 72, 84, 47, 116, 104, 101, 45, 112, 105, 116, 34, 125});
    }
    private HashMap<String, byte[]> buttons = new HashMap<>();
    private byte[] data;
    private int size;

    private void reset() {
        data = null;
        size = 0;
    }

    @Override
    public void onEnable() {
        reset();
    }

    @EventLink
    private final Listener<WorldChangeEvent> onWorld = event -> {
        reset();
    };

    private static void sendToServer(PacketBuffer buffer) {
        mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("germmod-netease", buffer));
    }

    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReciive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S3FPacketCustomPayload) {
            final S3FPacketCustomPayload wrapped = (S3FPacketCustomPayload) packet;

            if (wrapped.getChannelName().equalsIgnoreCase("REGISTER")) {
                String base = Joiner.on('\0').join(Arrays.asList("FML|HS", "FML", "FML|MP", "FML", "antimod", "ChatVexView", "Base64VexView", "HudBase64VexView", "FORGE", "germplugin-netease", "VexView", "hyt0", "armourers", "promotion"));

                mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.wrappedBuffer(base.getBytes(StandardCharsets.UTF_8)))));
            } else if (wrapped.getChannelName().equalsIgnoreCase("germplugin-netease")) {
                final PacketBuffer buffer = wrapped.getBufferData();

                final int packetId = buffer.readInt();

                if (packetId == -1) {
                    final boolean needResize = buffer.readBoolean();
                    final int newSize = buffer.readInt();
                    final boolean isLast = buffer.readBoolean();
                    final byte[] nextArray = buffer.readByteArray();

                    if (needResize) {
                        data = new byte[newSize];
                    }

                    System.arraycopy(nextArray, 0, data, size, nextArray.length);
                    size += nextArray.length;

                    if (isLast) {
                        ByteBuf byteBuf = Unpooled.wrappedBuffer(data);
                        final S3FPacketCustomPayload newWrapper = new S3FPacketCustomPayload("germplugin-netease", new PacketBuffer(byteBuf));

                        final PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(newWrapper);

                        Client.INSTANCE.getEventBus().handle(packetReceiveEvent);

                        if (!packetReceiveEvent.isCancelled())
                            newWrapper.processPacket(mc.getNetHandler());
                    }
                } else if (packetId == 73) {
                    final String type = buffer.readStringFromBuffer(32767);
                    final String name = buffer.readStringFromBuffer(32767);
//                    final String data = buffer.readStringFromBuffer(99999999);

                    if (type.equalsIgnoreCase("gui")) {
                        if (name.equalsIgnoreCase("mainmenu")) {
                            final PacketBuffer newData = new PacketBuffer(Unpooled.buffer());

                            newData.writeInt(4);
                            newData.writeInt(0);
                            newData.writeInt(0);
                            newData.writeString("mainmenu");
                            newData.writeString("mainmenu");
                            newData.writeString("mainmenu");

                            sendToServer(newData);

                            for(String n : buttons.keySet()){
                                ChatComponentText textComponents = new ChatComponentText("");
                                textComponents.appendSibling(createClickableText("§8[§f"+n+"§8] ","/germ-btn-click "+n));
                                mc.thePlayer.addChatComponentMessage(textComponents);
                            }
                        }
                    }
                } else if (packetId == 72) {
                    final PacketBuffer data = new PacketBuffer(Unpooled.buffer());

                    data.writeInt(16);
                    data.writeString("3.4.2");
                    data.writeString(Base64.getEncoder().encodeToString(("我是科比爱导管2333" + RandomUtil.randomName()).getBytes(StandardCharsets.UTF_8)));

                    sendToServer(data);
                }
            }
        }
    };

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        if(event.getPacket() instanceof C01PacketChatMessage){
            String message = ((C01PacketChatMessage) event.getPacket()).getMessage();
            if(message.startsWith("/germ-btn-click ")){
                String name = message.replace("/germ-btn-click ","");
                if(buttons.containsKey(name)){
                    sendToServer(new PacketBuffer(Unpooled.wrappedBuffer(buttons.get(name))));
                }
            }
        }
    };

    private static IChatComponent createClickableText(String text, String command) {
        ChatComponentText clickableText = new ChatComponentText(text);
        clickableText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return clickableText;
    }
}
