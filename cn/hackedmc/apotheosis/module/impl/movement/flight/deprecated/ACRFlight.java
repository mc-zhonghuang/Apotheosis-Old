package cn.hackedmc.apotheosis.module.impl.movement.flight.deprecated;

import cn.hackedmc.apotheosis.module.impl.movement.Flight;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.apotheosis.util.player.DamageUtil;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;

public class ACRFlight extends Mode<Flight> {

    private int offGroundTicks;

    public ACRFlight(String name, Flight parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, 3.42F, 1, false, false);
    }

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        MoveUtil.strafe(0.06);
        if (mc.thePlayer.ticksExisted % 3 == 0) {
            mc.thePlayer.motionY = 0.40444491418477213;
            offGroundTicks = 0;
        }

        if (offGroundTicks == 1) {
            MoveUtil.strafe(0.36);
            mc.thePlayer.motionY = 0.33319999363422365;
        }
    };

}
