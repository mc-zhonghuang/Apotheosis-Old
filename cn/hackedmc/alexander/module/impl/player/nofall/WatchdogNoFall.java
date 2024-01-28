package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.component.impl.player.BlinkComponent;
import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;
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