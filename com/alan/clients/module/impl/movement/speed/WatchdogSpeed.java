package com.alan.clients.module.impl.movement.speed;

import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author Alan
 * @since 18/11/2022
 */

public class WatchdogSpeed extends Mode<Speed> {
    private final NumberValue timer = new NumberValue("Timer", this, 1, 1, 2, 0.1);
    private final ModeValue mode = new ModeValue("Type", this)
            .add(new SubMode("Full Strafe"))
            .add(new SubMode("Ground Strafe"))
            .add(new SubMode("Damage Strafe"))
            .add(new SubMode("50% Strafe"))
            .setDefault("Full Strafe");

    private final BooleanValue damageBoost = new BooleanValue("Damage Boost", this, false);
    private float angle;

    public WatchdogSpeed(String name, Speed parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mode.getValue().getName().equals("Ground Strafe") || mode.getValue().getName().equals("Damage Strafe") || mode.getValue().getName().equals("50% Strafe"))
            return;

        if (!(PlayerUtil.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) && mc.thePlayer.ticksSinceVelocity > 20) {
            event.setOnGround(true);
        }

    };

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        if (mc.thePlayer.ticksSinceTeleport > 40) MoveUtil.useDiagonalSpeed();

        switch (mode.getValue().getName()) {

            case "Ground Strafe":
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            MoveUtil.strafe(Math.max(MoveUtil.speed(), 0.54));
                        }
                    } else {
                        if (mc.thePlayer.onGround) {
                            MoveUtil.strafe(Math.max(MoveUtil.speed(), MoveUtil.getAllowedHorizontalDistance()));
                            mc.thePlayer.jump();
                        }
                    }
                }

                break;

            case "Damage Strafe":
                if (mc.thePlayer.onGround && mc.thePlayer.ticksSinceJump > 5 && MoveUtil.isMoving()) {
                    MoveUtil.strafe();
                    MoveUtil.strafe((MoveUtil.getAllowedHorizontalDistance() - Math.random() / 100f));
                    mc.thePlayer.jump();
                }

                if (mc.thePlayer.ticksSincePlayerVelocity <= 20 && mc.thePlayer.ticksSinceTeleport > 20) {
                    MoveUtil.strafe();
                }
                break;

            case "Full Strafe":
                if (!(PlayerUtil.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) || !(PlayerUtil.blockRelativeToPlayer(0, -1.1, 0) instanceof BlockAir)) {
                    angle = MoveUtil.simulationStrafeAngle(angle, mc.thePlayer.ticksSinceVelocity < 40 ? 39.9f : 19.9f);
                }

                if (mc.thePlayer.ticksSinceVelocity <= 20 || mc.thePlayer.onGround) {
                    angle = MoveUtil.simulationStrafeAngle(angle, 360);
                }

//                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));

                if (mc.thePlayer.ticksSinceVelocity > 20) {
                    switch (mc.thePlayer.offGroundTicks) {
                        case 1:
                            mc.thePlayer.motionY -= 0.005;
                            break;

                        case 2:
                        case 3:
                            mc.thePlayer.motionY -= 0.001;
                            break;
                    }
                }

                if (mc.thePlayer.onGround) {
                    double lastAngle = Math.atan(mc.thePlayer.lastMotionX / mc.thePlayer.lastMotionZ) * (180 / Math.PI);

                    MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() - Math.random() / 1000);
                    mc.thePlayer.jump();

                    double angle = Math.atan(mc.thePlayer.motionX / mc.thePlayer.motionZ) * (180 / Math.PI);

                    if (Math.abs(lastAngle - angle) > 20 && mc.thePlayer.ticksSinceVelocity > 20) {
                        int speed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).amplifier + 1 : 0;

                        switch (speed) {
                            case 0:
                                MoveUtil.moveFlying(-0.005);
                                break;

                            case 1:
                                MoveUtil.moveFlying(-0.035);
                                break;

                            default:
                                MoveUtil.moveFlying(-0.04);
                                break;
                        }
                    }
                }
                break;
            case "50% Strafe":
                if (MoveUtil.isMoving()) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        if (mc.thePlayer.onGround) mc.thePlayer.jump();
                        if (mc.thePlayer.onGround || MoveUtil.speed() < 0.275) {
                            MoveUtil.strafe(Math.max(MoveUtil.speed(), 0.54));
                        }
                    } else {
                        if (mc.thePlayer.onGround || MoveUtil.speed() < 0.275) {
                            MoveUtil.strafe(Math.max(MoveUtil.speed(), 0.42));
                        }
                        if (mc.thePlayer.onGround) mc.thePlayer.jump();
                    }
                }

                break;
        }

        mc.timer.timerSpeed = timer.getValue().floatValue();
    };
}
