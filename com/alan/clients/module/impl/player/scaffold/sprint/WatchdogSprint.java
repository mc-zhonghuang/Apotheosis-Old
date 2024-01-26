package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class WatchdogSprint extends Mode<Scaffold> {
    public WatchdogSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        mc.gameSettings.keyBindSprint.setPressed(true);
        mc.thePlayer.setSprinting(true);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        mc.gameSettings.keyBindSprint.setPressed(true);
        mc.thePlayer.setSprinting(true);
    };

    @EventLink
    public final Listener<PacketSendEvent> onPacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer wrapper = (C03PacketPlayer) packet;

            if (mc.thePlayer.onGround && wrapper.moving && !mc.isSingleplayer()) {
                wrapper.y += 0.00625;
                wrapper.onGround = false;
            }
        }

        if (packet instanceof C08PacketPlayerBlockPlacement) {
            final C08PacketPlayerBlockPlacement wrapped = (C08PacketPlayerBlockPlacement) packet;

            if (wrapped.getPlacedBlockDirection() == 255) return;

            wrapped.facingX = (float) MathUtil.getRandom(0.1, 0.9);
            wrapped.facingY = (float) MathUtil.getRandom(0.1, 0.9);
            wrapped.facingZ = (float) MathUtil.getRandom(0.1, 0.9);
        }
    };
}
