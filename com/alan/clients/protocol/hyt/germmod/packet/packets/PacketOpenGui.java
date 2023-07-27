package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.PacketManager;
import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketOpenGui implements Packet {
    private final String guiId;

    public PacketOpenGui(String guiId) {
        this.guiId = guiId;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeInt(0);
        buffer.writeInt(0);
        buffer.writeString(guiId);
        buffer.writeString(guiId);
        buffer.writeString(guiId);
        PacketManager.debug("open a gui.");
    }

    @Override
    public void encode() {
    }

    @Override
    public int packetId() {
        return 4;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
