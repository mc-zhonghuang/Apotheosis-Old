package cn.hackedmc.alexander.module.impl.player.scaffold.sprint;

import cn.hackedmc.alexander.module.impl.player.Scaffold;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.util.math.MathUtil;
import cn.hackedmc.alexander.value.Mode;
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
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        mc.gameSettings.keyBindSprint.setPressed(true);
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
