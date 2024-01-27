package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.ItemDamageComponent;
import com.alan.clients.component.impl.player.PingSpoofComponent;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;

/**
 * @author LvZiQiao
 * @since 1/27/2024
 */

public class WatchdogLongJump extends Mode<LongJump> {
    private int ticks = 0;
    private int jumpTime = 0;

    public WatchdogLongJump(String name, LongJump parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ticks = 0;
        jumpTime = 0;
    }

    @EventLink
    public final Listener<MoveEvent> onMove = event -> {
        if (jumpTime < 4)
            event.zeroXZ();
    };

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (jumpTime < 4) {
            if (mc.thePlayer.onGround) {
                jumpTime++;
                mc.thePlayer.jump();
            }
            if (jumpTime != 4) event.setOnGround(false);
        } else {
            ticks++;
            if (ticks == 10) {
                BlinkComponent.blinking = true;
                MoveUtil.strafe(1);
                mc.thePlayer.jump();
            } else if (ticks > 10 && mc.thePlayer.onGround) {
                getParent().toggle();
                BlinkComponent.dispatch();
                BlinkComponent.blinking = false;
                NotificationComponent.post("Watchdog long jump", "After the jump.");
                this.ticks = 0;
            }
        }
    };
}