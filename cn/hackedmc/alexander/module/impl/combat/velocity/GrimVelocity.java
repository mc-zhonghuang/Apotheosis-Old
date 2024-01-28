package cn.hackedmc.alexander.module.impl.combat.velocity;

import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.module.impl.combat.Velocity;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;

public class GrimVelocity extends Mode<Velocity> {

    public boolean send = false;

    public GrimVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {

        Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;
            if (mc.theWorld.getEntityByID(wrapper.getEntityID()) == mc.thePlayer) {
                event.setCancelled();
                send = true;
            }
        }
        if (packet instanceof S27PacketExplosion) {
            event.setCancelled();
            send = true;
        }
    };

    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (send) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).up(), 1, mc.thePlayer.getHeldItem(), 0, 0, 0));
            send = false;
        }
    };
}
