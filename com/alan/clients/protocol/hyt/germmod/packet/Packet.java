package com.alan.clients.protocol.hyt.germmod.packet;

import net.minecraft.network.PacketBuffer;

public interface Packet {
    int packetId = Short.MAX_VALUE;

    void write(PacketBuffer buffer);

    void encode();

    int packetId();

    void read(PacketBuffer buffer);
}
