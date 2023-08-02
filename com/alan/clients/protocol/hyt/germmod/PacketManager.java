package com.alan.clients.protocol.hyt.germmod;

import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketChannel;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketDigging;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketGui;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.interfaces.InstanceAccess;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

public class PacketManager implements InstanceAccess {
    private static final HashMap<Integer, Packet> packets = new HashMap<>();
    public static final String channelName = "germmod-netease";
    public static final String serverChannelName = "germplugin-netease";

    private static void registerPacket(Packet packet) {
        packets.put(packet.packetId(), packet);
    }

    public static void init() {
        registerPacket(new PacketChannel());
        registerPacket(new PacketGui());
        registerPacket(new PacketDigging());
    }

    public static void processPacket(ByteBuf byteBuf) {
        final PacketBuffer buffer = new PacketBuffer(byteBuf);
        final int packetId = buffer.readInt();
        final Packet packet = packets.get(packetId);
        if (packet == null) return;
        packet.read(buffer);
        packet.encode();
    }

    public static void sendPacket(Packet packet) {
        final PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeInt(packet.packetId());
        packet.write(buffer);
        final C17PacketCustomPayload payload = new C17PacketCustomPayload(channelName, buffer);
        mc.thePlayer.sendQueue.addToSendQueueUnregistered(payload);
    }

    public static void debug(String text) {
        ChatUtil.display(EnumChatFormatting.YELLOW + "[Protocol-GermMod] " + text);
    }
}
