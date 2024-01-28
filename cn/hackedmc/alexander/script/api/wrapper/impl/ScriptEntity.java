package cn.hackedmc.alexander.script.api.wrapper.impl;

import cn.hackedmc.alexander.script.api.API;
import cn.hackedmc.alexander.script.api.wrapper.ScriptWrapper;
import cn.hackedmc.alexander.script.api.wrapper.impl.vector.ScriptVector2;
import cn.hackedmc.alexander.script.api.wrapper.impl.vector.ScriptVector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

/**
 * @author Strikeless
 * @since 20.06.2022
 */
public class ScriptEntity extends ScriptWrapper<Entity> {

    public ScriptEntity(final Entity wrapped) {
        super(wrapped);
    }

    static ScriptEntity getById(final int id) {
        final Entity entity = API.MC.theWorld.getEntityByID(id);
        return tryInstantiate(entity);
    }

    static ScriptEntity getByName(final String name) {
        final Entity entity = API.MC.theWorld.getPlayerEntityByName(name);
        return tryInstantiate(entity);
    }

    private static ScriptEntity tryInstantiate(final Entity entity) {
        // yes yes this method was totally necessary
        return new ScriptEntity(entity);
    }

    public boolean isLiving() {
        return this.wrapped instanceof EntityLivingBase;
    }

    public ScriptVector3 getPosition() {
        return new ScriptVector3(this.wrapped.posX, this.wrapped.posY, this.wrapped.posZ);
    }

    public ScriptVector3 getLastPosition() {
        return new ScriptVector3(this.wrapped.lastTickPosX, this.wrapped.lastTickPosY, this.wrapped.lastTickPosZ);
    }

    public ScriptVector3 getMotion() {
        return new ScriptVector3(this.wrapped.motionX, this.wrapped.motionY, this.wrapped.motionZ);
    }

    public ScriptVector2 getRotation() {
        return new ScriptVector2(this.wrapped.rotationYaw, this.wrapped.rotationPitch);
    }

    public ScriptVector2 getLastRotation() {
        return new ScriptVector2(this.wrapped.prevRotationYaw, this.wrapped.prevRotationPitch);
    }

    public int getTicksExisted() {
        return this.wrapped.ticksExisted;
    }

    public int getEntityId() {
        return this.wrapped.getEntityId();
    }

    public String getDisplayName() {
        return this.wrapped.getDisplayName().getUnformattedTextForChat();
    }

    public ScriptInventory getInventory() {
        return new ScriptInventory(this.wrapped.getInventory());
    }

    public float getDistanceToEntity(final ScriptEntity entity) {
        final float f = (float) (this.wrapped.posX - entity.getPosition().getX().doubleValue());
        final float f1 = (float) (this.wrapped.posY - entity.getPosition().getY().doubleValue());
        final float f2 = (float) (this.wrapped.posZ - entity.getPosition().getZ().doubleValue());
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public double getDistance(final double x, final double y, final double z) {
        final double d0 = this.wrapped.posX - x;
        final double d1 = this.wrapped.posY - y;
        final double d2 = this.wrapped.posZ - z;
        return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
    }
}