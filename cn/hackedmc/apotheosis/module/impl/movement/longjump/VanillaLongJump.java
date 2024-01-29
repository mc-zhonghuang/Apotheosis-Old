package cn.hackedmc.apotheosis.module.impl.movement.longjump;

import cn.hackedmc.apotheosis.module.impl.movement.LongJump;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;

/**
 * @author Auth
 * @since 3/02/2022
 */
public class VanillaLongJump extends Mode<LongJump> {

    private final NumberValue height = new NumberValue("Height", this, 0.5, 0.1, 1, 0.01);
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);

    public VanillaLongJump(String name, LongJump parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {

        if (InstanceAccess.mc.thePlayer.onGround) {
            InstanceAccess.mc.thePlayer.motionY = height.getValue().floatValue();
        }

        event.setSpeed(speed.getValue().floatValue());
    };

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}