package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.PacketManager;
import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import com.alan.clients.protocol.hyt.germmod.util.UuidUtil;
import net.minecraft.network.PacketBuffer;

public class PacketDigging implements Packet {
    @Override
    public void write(PacketBuffer buffer) {
    }

    @Override
    public void encode() {
        if (PacketChannel.sendLoginPacket) {
            PacketManager.sendPacket(new PacketRegister("3.4.2", UuidUtil.getUuid()));
            PacketChannel.sendLoginPacket = false;
            PacketManager.debug("login was successful!");
        }
    }

    @Override
    public int packetId() {
        return 72;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
