package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import com.alan.clients.protocol.hyt.germmod.util.GuiState;
import net.minecraft.network.PacketBuffer;

public class PacketGuiState implements Packet {
    private final String guiId;
    private final String buttonId;
    private final int state;

    public PacketGuiState(String guiId, String buttonId, GuiState state) {
        this.guiId = guiId;
        this.buttonId = buttonId;
        this.state = state.getId();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(guiId);
        buffer.writeString(buttonId);
        buffer.writeInt(state);
    }

    @Override
    public void encode() {
    }

    @Override
    public int packetId() {
        return 13;
    }

    @Override
    public void read(PacketBuffer buffer) {
    }
}
