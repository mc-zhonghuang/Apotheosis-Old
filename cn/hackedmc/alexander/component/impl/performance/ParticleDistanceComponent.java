package cn.hackedmc.alexander.component.impl.performance;

import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2APacketParticles;

public class ParticleDistanceComponent extends Component {

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {

        final Packet<?> packet = event.getPacket();

        if (packet instanceof S2APacketParticles) {
            final S2APacketParticles wrapper = ((S2APacketParticles) packet);

            final double distance = mc.thePlayer.getDistanceSq(wrapper.getXCoordinate(), wrapper.getYCoordinate(), wrapper.getZCoordinate());

            if (distance > 6 * 6) {
                event.setCancelled(true);
            }
        }
    };
}