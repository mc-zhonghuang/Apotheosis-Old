package cn.hackedmc.apotheosis.module.impl.other.protocols;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.other.ServerProtocol;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.util.RandomUtil;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
import cn.hackedmc.apotheosis.value.Mode;
import com.google.common.base.Joiner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class HuaYuTingProtocol extends Mode<ServerProtocol> {
    public HuaYuTingProtocol(String name, ServerProtocol parent) {
        super(name, parent);
    }
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
                    final String data = buffer.readStringFromBuffer(99999999);

                    ChatUtil.display(type);
                    ChatUtil.display(name);
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
}
