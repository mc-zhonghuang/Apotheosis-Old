package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketChannel implements Packet {
    public static boolean sendLoginPacket = false;

    @Override
    public void write(PacketBuffer buffer) {
    }

    @Override
    public void encode() {
        sendLoginPacket = true;
    }

    @Override
    public int packetId() {
        return 731;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
