package cn.hackedmc.apotheosis.module.impl.movement.speed;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.component.impl.player.RotationComponent;
import cn.hackedmc.apotheosis.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.apotheosis.module.impl.movement.Speed;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;

/**
 * @author Alan
 * @since 18/11/2022
 */

public class LegitSpeed extends Mode<Speed> {

    private ModeValue rotationExploit = new ModeValue("Rotation Exploit Mode", this)
            .add(new SubMode("Off"))
            .add(new SubMode("1.9+ BoundingBox"))
            .add(new SubMode("Rotate (Fully Legit)"))
            .add(new SubMode("Speed Equivalent (Almost legit, Very hard to flag)"))
            .setDefault("Speed Equivalent (Almost legit, Very hard to flag)");
    private BooleanValue cpuSpeedUpExploit = new BooleanValue("CPU SpeedUp Exploit", this, true);
    private BooleanValue noJumpDelay = new BooleanValue("No Jump Delay", this, true);

    public LegitSpeed(String name, Speed parent) {
        super(name, parent);
    }


    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<PreUpdateEvent> preUpdate = event -> {
        switch (rotationExploit.getValue().getName()) {
            case "Rotate (Fully Legit)":
                if (!mc.thePlayer.onGround)
                    RotationComponent.setRotations(new Vector2f(mc.thePlayer.rotationYaw + 45, mc.thePlayer.rotationPitch), 10, MovementFix.NORMAL);
                break;

            case "Speed Equivalent (Almost legit, Very hard to flag)":
                MoveUtil.useDiagonalSpeed();
                break;

            case "1.9+ BoundingBox":
                if (!Client.INSTANCE.getTargetManager().getTargets(1F).isEmpty()) {
                    if (MoveUtil.speed() <= 5F) {
                        mc.thePlayer.motionX *= 1.1;
                        mc.thePlayer.motionZ *= 1.1;
                    }
                }

                break;
        }

        if (noJumpDelay.getValue()) {
            mc.thePlayer.jumpTicks = 0;
        }

        if (cpuSpeedUpExploit.getValue()) {
            mc.timer.timerSpeed = 1.004f;
        }
    };

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<StrafeEvent> strafe = event -> {
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    };
}
