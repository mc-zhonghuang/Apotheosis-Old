package cn.hackedmc.alexander.module.impl.movement.flight;

import cn.hackedmc.alexander.module.impl.movement.Flight;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;

/**
 * @author Alan
 * @since 03.07.2022
 */
public class GrimFlight extends Mode<Flight> {

    public GrimFlight(String name, Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ChatUtil.display("Any blocks you place will be ghost blocks.");
    }

    @Override
    public void onDisable() {

    }

    @EventLink
    private Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S23PacketBlockChange) {
            event.setCancelled();
        }
    };
}
