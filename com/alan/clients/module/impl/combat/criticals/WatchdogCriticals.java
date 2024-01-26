package com.alan.clients.module.impl.combat.criticals;

import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class WatchdogCriticals extends Mode<Criticals> {

    public WatchdogCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    boolean attacking = false;

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            if (attacking) {
                final C03PacketPlayer wrapped = (C03PacketPlayer) packet;

                if (wrapped.onGround) {
                    if (wrapped.moving) {
                        wrapped.y += 0.00625;
                        wrapped.setOnGround(false);
                    } else {
                        event.setCancelled();

                        if (wrapped.rotating) {
                            mc.getNetHandler().addToSendQueueUnregistered(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 0.00625, mc.thePlayer.posZ, wrapped.yaw, wrapped.pitch, false));
                        } else {
                            mc.getNetHandler().addToSendQueueUnregistered(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00625, mc.thePlayer.posZ, false));
                        }
                    }
                }

                attacking = false;
            }
        }

        if (packet instanceof C02PacketUseEntity) {
            final C02PacketUseEntity wrapped = (C02PacketUseEntity) packet;

            if (wrapped.getAction() == C02PacketUseEntity.Action.ATTACK) {
                attacking = true;
            }
        }
    };
}
