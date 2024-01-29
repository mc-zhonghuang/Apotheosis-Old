package cn.hackedmc.apotheosis.module.impl.player;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.BoundsNumberValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;

/**
 * @author Alan
 * @since 23/10/2021
 */

@Rise
@ModuleInfo(name = "module.player.blink.name", description = "module.player.blink.description", category = Category.PLAYER)
public class Blink extends Module {

    public BooleanValue pulse = new BooleanValue("Pulse", this, false);
    public BooleanValue incoming = new BooleanValue("Incoming", this, false);
    public BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 2, 2, 2, 40, 1, () -> !pulse.getValue());
    public int next;
    private EntityOtherPlayerMP blinkEntity;

    @Override
    protected void onEnable() {
        getNext();
        spawnEntity();
        BlinkComponent.blinking = true;
    }

    @Override
    protected void onDisable() {
        deSpawnEntity();
        BlinkComponent.blinking = false;
    }

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> getNext();

    public void getNext() {
        if (InstanceAccess.mc.thePlayer == null) return;
        next = InstanceAccess.mc.thePlayer.ticksExisted + (int) MathUtil.getRandom(delay.getValue().intValue(), delay.getSecondValue().intValue());
    }

    public void deSpawnEntity() {
        if (blinkEntity != null) {
            Client.INSTANCE.getBotManager().remove(blinkEntity);
            InstanceAccess.mc.theWorld.removeEntityFromWorld(blinkEntity.getEntityId());
            blinkEntity = null;
        }
    }

    public void spawnEntity() {
        if (blinkEntity == null) {
            blinkEntity = new EntityOtherPlayerMP(InstanceAccess.mc.theWorld, InstanceAccess.mc.thePlayer.getGameProfile());
            blinkEntity.setPositionAndRotation(InstanceAccess.mc.thePlayer.posX, InstanceAccess.mc.thePlayer.posY, InstanceAccess.mc.thePlayer.posZ, InstanceAccess.mc.thePlayer.rotationYaw, InstanceAccess.mc.thePlayer.rotationPitch);
            blinkEntity.rotationYawHead = InstanceAccess.mc.thePlayer.rotationYawHead;
            blinkEntity.setSprinting(InstanceAccess.mc.thePlayer.isSprinting());
            blinkEntity.setInvisible(InstanceAccess.mc.thePlayer.isInvisible());
            blinkEntity.setSneaking(InstanceAccess.mc.thePlayer.isSneaking());
            blinkEntity.inventory = InstanceAccess.mc.thePlayer.inventory;
            Client.INSTANCE.getBotManager().add(blinkEntity);

            InstanceAccess.mc.theWorld.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
        }
    }
}