package cn.hackedmc.apotheosis.util.packet;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.exploit.Disabler;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import lombok.experimental.UtilityClass;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

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
    public static void sendPacketC0F() {
        if (!Disabler.INSTANCE.grimACDisabler.post.getValue()) {
            send(new C0FPacketConfirmTransaction(MathUtil.getRandom2(102, 1000024123), (short)MathUtil.getRandom2(102, 1000024123), true));
        }

    }

    public static void sendPacketC0F(boolean noEvent) {
        if (!Disabler.INSTANCE.grimACDisabler.post.getValue()) {
            if (!noEvent) {
                send(new C0FPacketConfirmTransaction(MathUtil.getRandom2(102, 1000024123), (short)MathUtil.getRandom2(102, 1000024123), true));
            } else {
                sendNoEvent(new C0FPacketConfirmTransaction(MathUtil.getRandom2(102, 1000024123), (short)MathUtil.getRandom2(102, 1000024123), true));
            }
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
