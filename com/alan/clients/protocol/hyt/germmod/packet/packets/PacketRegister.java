package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketRegister implements Packet {
    private final String version;
    private final String uuid;

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.version);
        buffer.writeString(this.uuid);
    }

    public PacketRegister(String version, String uuid) {
        this.version = version;
        this.uuid = uuid;
    }

    @Override
    public void encode() {
    }

    @Override
    public int packetId() {
        return 16;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
