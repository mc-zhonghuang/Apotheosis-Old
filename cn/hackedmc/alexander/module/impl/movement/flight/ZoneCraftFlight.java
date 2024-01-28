package cn.hackedmc.alexander.module.impl.movement.flight;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.module.impl.movement.Flight;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.alexander.util.player.MoveUtil;
import cn.hackedmc.alexander.component.impl.render.NotificationComponent;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.util.Vec3;

public class ZoneCraftFlight extends Mode<Flight> {

    public ZoneCraftFlight(String name, Flight parent) {
        super(name, parent);
    }

    public Vec3 position = new Vec3(0, 0, 0);

    @Override
    public void onEnable() {
        if (!mc.thePlayer.onGround) {
            toggle();
        }

        if (!Client.DEVELOPMENT_SWITCH) {
            NotificationComponent.post("Flight", "This feature is only enabled for developers atm");
        }

        position = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        event.setPosX(position.xCoord);
        event.setPosY(position.yCoord);
        event.setPosZ(position.zCoord);
        event.setOnGround(true);
    };

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        final float speed = 3;

        event.setSpeed(speed);

        mc.thePlayer.motionY = 0.0D;
    };

}
