package cn.hackedmc.alexander.util.packet;

import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import lombok.experimental.UtilityClass;
import net.minecraft.network.Packet;

@UtilityClass
public final class PacketUtil implements InstanceAccess {

    public void send(final Packet<?> packet) {
        mc.getNetHandler().addToSendQueue(packet);
    }

    public void sendNoEvent(final Packet<?> packet) {
        mc.getNetHandler().addToSendQueueUnregistered(packet);
    }

    public void queue(final Packet<?> packet) {
        if (isServerPacket(packet)) {
            mc.getNetHandler().addToReceiveQueue(packet);
        } else {
            mc.getNetHandler().addToSendQueue(packet);
        }
    }
    public static void sendToServer(PacketWrapper packet, Class<? extends Protocol> packetProtocol) {
        sendToServer(packet, packetProtocol);
    }
    public void queueNoEvent(final Packet<?> packet) {
        if (isServerPacket(packet)) {
            mc.getNetHandler().addToReceiveQueueUnregistered(packet);
        } else {
            mc.getNetHandler().addToSendQueueUnregistered(packet);
        }
    }

    public void receive(final Packet<?> packet) {
        mc.getNetHandler().addToReceiveQueue(packet);
    }

    public void receiveNoEvent(final Packet<?> packet) {
        mc.getNetHandler().addToReceiveQueueUnregistered(packet);
    }

    private boolean isServerPacket(final Packet<?> packet) {
        return packet.toString().toCharArray()[34] == 'S';
    }

    private boolean isClientPacket(final Packet<?> packet) {
        return packet.toString().toCharArray()[34] == 'C';
    }

    public static class TimedPacket {
        private final Packet<?> packet;
        private final long time;

        public TimedPacket(final Packet<?> packet, final long time) {
            this.packet = packet;
            this.time = time;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public long getTime() {
            return time;
        }
    }
}
