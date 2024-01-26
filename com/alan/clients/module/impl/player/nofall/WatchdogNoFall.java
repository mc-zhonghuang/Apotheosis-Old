package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.value.Mode;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author LvZiQiao
 * @since 25.1.2024
 */
public class WatchdogNoFall extends Mode<NoFall> {

    public WatchdogNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (FallDistanceComponent.distance > 1) {
            BlinkComponent.blinking = true;
        } else if (mc.thePlayer.onGround) {
            BlinkComponent.blinking = false;
        }
    };

    @EventLink()
    private final Listener<PacketSendEvent> onPacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            if (FallDistanceComponent.distance > 2.5) {
                ((C03PacketPlayer) packet).setOnGround(true);
                FallDistanceComponent.distance = 0;
            }
        }
    };
}