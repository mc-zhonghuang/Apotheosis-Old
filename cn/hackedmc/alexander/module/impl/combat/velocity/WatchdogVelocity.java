package cn.hackedmc.alexander.module.impl.combat.velocity;

import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.module.impl.combat.Velocity;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public final class WatchdogVelocity extends Mode<Velocity> {
    public WatchdogVelocity(String name, Velocity parent) {
        super(name, parent);
    }

    private final BooleanValue speedLimit = new BooleanValue("Speed Limit",this , true);
    private final NumberValue speedMaxValue = new NumberValue("Max Speed", this, 0.5, 0.1, 1.0, 0.1, () -> !speedLimit.getValue());

    @EventLink()
    public final Listener<PreMotionEvent> onMotion = event -> {
        if (mc.thePlayer.hurtTime == 9)
            if (!MoveUtil.isMoving()) {
                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
            }
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
                    if (mc.thePlayer.onGround) mc.thePlayer.jump();
                    if (!speedLimit.getValue()) MoveUtil.strafe();
                    else MoveUtil.strafe(Math.min(speedMaxValue.getValue().doubleValue(), MoveUtil.speed()));
                }
            }
        }
    };
}
