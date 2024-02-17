package cn.hackedmc.apotheosis.module.impl.player.scaffold.sprint;

import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class WatchdogSprint extends Mode<Scaffold> {
    public WatchdogSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        BlinkComponent.blinking = true;
        mc.gameSettings.keyBindSprint.setPressed(true);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        mc.gameSettings.keyBindSprint.setPressed(true);
        if (mc.thePlayer.ticksExisted % 3 == 0)
            BlinkComponent.dispatch();
    };

    @Override
    public void onDisable() {
        BlinkComponent.dispatch();
        BlinkComponent.blinking = false;
    }
}
