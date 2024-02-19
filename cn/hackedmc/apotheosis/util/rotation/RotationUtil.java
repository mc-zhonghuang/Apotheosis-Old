package cn.hackedmc.apotheosis.util.rotation;

import cn.hackedmc.apotheosis.util.RayCastUtil;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.math.MathConst;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.util.vector.Vector3d;
import com.google.common.base.Predicates;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;

import java.util.List;

/**
 * @author Patrick
 * @since 11/17/2021
 */

@UtilityClass
public class RotationUtil implements InstanceAccess {
    public static boolean canAimAtTarget(float yaw, float pitch, Entity target,float reach) {
        final float range = reach; // 设置射程范围，根据需要进行调整
        final float partialTicks = mc.timer.renderPartialTicks;
        final Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;

        if (entity != null && mc.theWorld != null) {
            mc.mcProfiler.startSection("pick");
            final double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            mc.mcProfiler.endSection();

            return pointedEntity == target;
        }

        return false;
    }
    public static Vector2f getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP entityplayersp = mc.thePlayer;
        double d0 = posX - entityplayersp.posX;
        double d1 = posY - (entityplayersp.posY + (double)entityplayersp.getEyeHeight());
        double d2 = posZ - entityplayersp.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
        return new Vector2f(f, f1);
    }

    public static Vector2f getRotationsNonLivingEntity(Entity entity) {
        return getRotations(entity.posX, entity.posY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5D, entity.posZ);
    }
    public Vector2f calculate(final Vector3d from, final Vector3d to) {
        final Vector3d diff = to.subtract(from);
        final double distance = Math.hypot(diff.getX(), diff.getZ());
        final float yaw = (float) (MathHelper.atan2(diff.getZ(), diff.getX()) * MathConst.TO_DEGREES) - 90.0F;
        final float pitch = (float) (-(MathHelper.atan2(diff.getY(), distance) * MathConst.TO_DEGREES));
        return new Vector2f(yaw, pitch);
    }

    public Vector2f calculate(final Entity entity) {
        return calculate(entity.getCustomPositionVector().add(0, Math.max(0, Math.min(mc.thePlayer.posY - entity.posY +
                mc.thePlayer.getEyeHeight(), (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.9)), 0));
    }

    public Vector2f calculate(final Entity entity, final boolean adaptive, final double range) {
        Vector2f normalRotations = calculate(entity);
        if (!adaptive || RayCastUtil.rayCast(normalRotations, range).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return normalRotations;
        }

        for (double yPercent = 1; yPercent >= 0; yPercent -= 0.25) {
            for (double xPercent = 1; xPercent >= -0.5; xPercent -= 0.5) {
                for (double zPercent = 1; zPercent >= -0.5; zPercent -= 0.5) {
                    Vector2f adaptiveRotations = calculate(entity.getCustomPositionVector().add(
                            (entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) * xPercent,
                            (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * yPercent,
                            (entity.getEntityBoundingBox().maxZ - entity.getEntityBoundingBox().minZ) * zPercent));

                    if (RayCastUtil.rayCast(adaptiveRotations, range).typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        return adaptiveRotations;
                    }
                }
            }
        }

        return normalRotations;
    }

    public Vector2f calculate(final Vec3 to, final EnumFacing enumFacing) {
        return calculate(new Vector3d(to.xCoord, to.yCoord, to.zCoord), enumFacing);
    }

    public Vector2f calculate(final Vec3 to) {
        return calculate(mc.thePlayer.getCustomPositionVector().add(0, mc.thePlayer.getEyeHeight(), 0), new Vector3d(to.xCoord, to.yCoord, to.zCoord));
    }

    public Vector2f calculate(final Vector3d to) {
        return calculate(mc.thePlayer.getCustomPositionVector().add(0, mc.thePlayer.getEyeHeight(), 0), to);
    }

    public Vector2f getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX + (double) face.getFrontOffsetX() / 2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ + (double) face.getFrontOffsetZ() / 2;
        double y = (block.getY() + 0.5);
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 82.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new Vector2f(yaw, pitch);
    }

    public Vector2f calculate(final Vector3d position, final EnumFacing enumFacing) {
        double x = position.getX() + 0.5D;
        double y = position.getY() + 0.5D;
        double z = position.getZ() + 0.5D;

        x += (double) enumFacing.getDirectionVec().getX() * 0.5D;
        y += (double) enumFacing.getDirectionVec().getY() * 0.5D;
        z += (double) enumFacing.getDirectionVec().getZ() * 0.5D;
        return calculate(new Vector3d(x, y, z));
    }

    public Vector2f applySensitivityPatch(final Vector2f rotation) {
        final Vector2f previousRotation = mc.thePlayer.getPreviousRotation();
        final float mouseSensitivity = (float) (mc.gameSettings.mouseSensitivity * (1 + Math.random() / 10000000) * 0.6F + 0.2F);
        final double multiplier = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F * 0.15D;
        final float yaw = previousRotation.x + (float) (Math.round((rotation.x - previousRotation.x) / multiplier) * multiplier);
        final float pitch = previousRotation.y + (float) (Math.round((rotation.y - previousRotation.y) / multiplier) * multiplier);
        return new Vector2f(yaw, MathHelper.clamp_float(pitch, -90, 90));
    }

    public Vector2f applySensitivityPatch(final Vector2f rotation, final Vector2f previousRotation) {
        final float mouseSensitivity = (float) (mc.gameSettings.mouseSensitivity * (1 + Math.random() / 10000000) * 0.6F + 0.2F);
        final double multiplier = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F * 0.15D;
        final float yaw = previousRotation.x + (float) (Math.round((rotation.x - previousRotation.x) / multiplier) * multiplier);
        final float pitch = previousRotation.y + (float) (Math.round((rotation.y - previousRotation.y) / multiplier) * multiplier);
        return new Vector2f(yaw, MathHelper.clamp_float(pitch, -90, 90));
    }

    public Vector2f relateToPlayerRotation(final Vector2f rotation) {
        final Vector2f previousRotation = mc.thePlayer.getPreviousRotation();
        final float yaw = previousRotation.x + MathHelper.wrapAngleTo180_float(rotation.x - previousRotation.x);
        final float pitch = MathHelper.clamp_float(rotation.y, -90, 90);
        return new Vector2f(yaw, pitch);
    }

    public Vector2f resetRotation(final Vector2f rotation) {
        if (rotation == null) {
            return null;
        }

        final float yaw = rotation.x + MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - rotation.x);
        final float pitch = mc.thePlayer.rotationPitch;
        return new Vector2f(yaw, pitch);
    }

    public Vector2f smooth(final Vector2f lastRotation, final Vector2f targetRotation, final double speed) {
        float yaw = targetRotation.x;
        float pitch = targetRotation.y;
        final float lastYaw = lastRotation.x;
        final float lastPitch = lastRotation.y;

        if (speed != 0) {
            final float rotationSpeed = (float) speed;

            final double deltaYaw = MathHelper.wrapAngleTo180_float(targetRotation.x - lastRotation.x);
            final double deltaPitch = pitch - lastPitch;

            final double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
            final double distributionYaw = Math.abs(deltaYaw / distance);
            final double distributionPitch = Math.abs(deltaPitch / distance);

            final double maxYaw = rotationSpeed * distributionYaw;
            final double maxPitch = rotationSpeed * distributionPitch;

            final float moveYaw = (float) Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
            final float movePitch = (float) Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);

            yaw = lastYaw + moveYaw;
            pitch = lastPitch + movePitch;

            for (int i = 1; i <= (int) (Minecraft.getDebugFPS() / 20f + Math.random() * 10); ++i) {

                if (Math.abs(moveYaw) + Math.abs(movePitch) > 1) {
                    yaw += (Math.random() - 0.5) / 1000;
                    pitch -= Math.random() / 200;
                }

                /*
                 * Fixing GCD
                 */
                final Vector2f rotations = new Vector2f(yaw, pitch);
                final Vector2f fixedRotations = RotationUtil.applySensitivityPatch(rotations);

                /*
                 * Setting rotations
                 */
                yaw = fixedRotations.x;
                pitch = Math.max(-90, Math.min(90, fixedRotations.y));
            }
        }

        return new Vector2f(yaw, pitch);
    }
}