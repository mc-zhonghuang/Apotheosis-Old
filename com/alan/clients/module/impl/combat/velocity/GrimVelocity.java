package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class GrimVelocity extends Mode<Velocity> {
    private final NumberValue AutoSilent = new NumberValue("AutoSilent", this, 1, 0, 10, 1);
    private final BooleanValue debug = new BooleanValue("Debug", this, false);
    private boolean jump;
    private int cancelPackets = 0;
    private int resetPersec = 8;
    private int updates = 0;
    private int S08 = 0;

    private final LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<>();

    private final LinkedList<Packet<NetHandlerPlayClient>> inBus = new LinkedList<>();
    private Boolean disableLogger = false;
    public GrimVelocity(String name, Velocity parent) {
        super(name, parent);
    }
    @Override
    public void onEnable() {
        if (mc.thePlayer == null){
            return;
        }
        inBus.clear();
        cancelPackets = 0;
    }
    @Override
    public void onDisable() {
        if (mc.thePlayer == null){
            return;
        }
        inBus.clear();
        cancelPackets = 0;
        blink();
    }
    public final void debug(String s){
        if (debug.getValue())
            ChatUtil.display(s);
    }
    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {

        Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {

            S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity) packet;
            if (mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) != mc.thePlayer) {
                event.setCancelled();
                cancelPackets = 3;
            }

        }

        if(cancelPackets > 0){
            if (mc.thePlayer == null || disableLogger ) return;
            if (packet instanceof S12PacketEntityVelocity && (mc.theWorld.getEntityByID(((S12PacketEntityVelocity) packet).getEntityID()) == mc.thePlayer)) {
                return;
            }
            event.setCancelled();
            inBus.add((Packet<NetHandlerPlayClient>) packet);
        }

    };
    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (MoveUtil.isMoving()) {
            updates++;
            if (resetPersec > 0) {
                if (updates >= 0) {
                    updates = 0;
                    if (cancelPackets > 0) {
                        cancelPackets--;
                    }
                }
            }
            if (cancelPackets == 0) {
                blink();
            }
        }

    };


    private void blink() {
        try {
            disableLogger = true;
            while (!packets.isEmpty()) {
                mc.getNetHandler().getNetworkManager().sendPacket(packets.take());
            }
            while (!inBus.isEmpty()) {
                inBus.poll().processPacket(mc.thePlayer.sendQueue);
            }
            disableLogger = false;
        } catch (Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }
    }
}
