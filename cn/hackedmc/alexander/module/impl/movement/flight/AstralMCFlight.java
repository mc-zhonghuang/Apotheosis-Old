package cn.hackedmc.alexander.module.impl.movement.flight;

import cn.hackedmc.alexander.module.impl.movement.Flight;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.TeleportEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Alan
 * @since 03.07.2022
 */
public class AstralMCFlight extends Mode<Flight> {

    private NumberValue height = new NumberValue("Height", this, 1, 0.1, 10, 0.1);
    private NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 10, 0.1);

    public AstralMCFlight(String name, Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        ChatUtil.display("Place a block to fly");
    }

    @EventLink
    private Listener<TeleportEvent> teleport = event -> {
        mc.thePlayer.motionY = height.getValue().doubleValue();
        MoveUtil.strafe(speed.getValue().doubleValue());
        event.setCancelled(true);
        mc.thePlayer.setPosition(event.getPosX(), event.getPosY(), event.getPosZ());
        PacketUtil.send(new C03PacketPlayer.C06PacketPlayerPosLook(event.getPosX(), event.getPosY(), event.getPosZ(), event.getYaw(), event.getPitch(), false));
    };

    @EventLink
    private Listener<PreMotionEvent> preMotion = event -> {
    };
}
