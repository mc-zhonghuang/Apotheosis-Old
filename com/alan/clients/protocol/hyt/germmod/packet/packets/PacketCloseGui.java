package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketCloseGui implements Packet {
    private final String guiId;

    public PacketCloseGui(String guiId) {
        this.guiId = guiId;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(guiId);
    }

    @Override
    public void encode() {
    }

    @Override
    public int packetId() {
        return 11;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
