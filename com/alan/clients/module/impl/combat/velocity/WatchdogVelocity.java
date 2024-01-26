package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import util.time.StopWatch;

public final class WatchdogVelocity extends Mode<Velocity> {
    public WatchdogVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    private final BooleanValue speedLimit = new BooleanValue("Speed Limit",this , true);
    private final NumberValue speedMaxValue = new NumberValue("Max Speed", this, 0.5, 0.1, 1.0, 0.1, () -> !speedLimit.getValue());

    @EventLink()
    public final Listener<PreMotionEvent> onMotion = event -> {
        if (mc.thePlayer.hurtTime == 9)
            if (!MoveUtil.isMoving())
                MoveUtil.strafe(0);
    };

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        if (getParent().onSwing.getValue() || getParent().onSprint.getValue() && !mc.thePlayer.isSwingInProgress)
            return;

        final Packet<?> p = event.getPacket();

        if (p instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) p;

            if (wrapper.getEntityID() == mc.thePlayer.getEntityId()) {
                if (MoveUtil.isMoving()) {
                    event.setCancelled();
                    mc.thePlayer.addVelocity(wrapper.motionX / 8000.0, wrapper.motionY / 8000.0, wrapper.motionZ / 8000.0);
                    if (!speedLimit.getValue()) MoveUtil.strafe();
                    else MoveUtil.strafe(Math.min(speedMaxValue.getValue().doubleValue(), MoveUtil.speed()));
                }
            }
        } else if (p instanceof S27PacketExplosion) {
            event.setCancelled(true);
        }
    };
}
