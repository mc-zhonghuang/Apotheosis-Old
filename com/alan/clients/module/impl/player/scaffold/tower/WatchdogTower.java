package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class WatchdogTower extends Mode<Scaffold> {
    public WatchdogTower(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (!mc.gameSettings.keyBindJump.isKeyDown()) return;

        if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.42 - Math.random() / 10000;
        } else if (mc.thePlayer.motionY >= 0.16 && mc.thePlayer.motionY <= 0.17) {
            mc.thePlayer.motionY = 0;
        }
    };
}
