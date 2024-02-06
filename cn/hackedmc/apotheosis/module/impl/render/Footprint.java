package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render3DEvent;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@ModuleInfo(name = "module.render.footprint.name", description = "module.render.footprint.description", category = Category.RENDER)
public class Footprint extends Module {
    private final NumberValue amount = new NumberValue("Amount", this, 6, 1, 20, 1);
    public static Footprint INSTANCE;
    public final HashMap<EntityPlayer, Short> positions = new HashMap<>();

    @Override
    protected void onEnable() {
        positions.clear();
    }

    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = event -> {
        positions.clear();
    };

    @EventLink
    private final Listener<PreUpdateEvent> onPreUpdate = event -> {
        for (Map.Entry<EntityPlayer, Short> entry : positions.entrySet()) {
            entry.setValue((short) (entry.getValue() + 1));

            if (entry.getValue() >= amount.getValue().shortValue())
                positions.remove(entry.getKey(), entry.getValue());
        }
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        final float partialTicks = mc.timer.renderPartialTicks;
        for (Map.Entry<EntityPlayer, Short> entry : positions.entrySet()) {
            final EntityPlayer player = entry.getKey();
            final short tick = entry.getValue();

            final Render<Entity> render = mc.getRenderManager().getEntityRenderObject(player);

            if (mc.getRenderManager() == null) {
                continue;
            }

            final Color color = ColorUtil.withAlpha(ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getSecondColor(), tick / amount.getValue().doubleValue()), (int) (255 * (1 - (tick / amount.getValue().doubleValue()))));

            final double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;

            RendererLivingEntity.setShaderBrightness(color);
            render.doRender(player, x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ, yaw, partialTicks);
            RendererLivingEntity.unsetShaderBrightness();

            player.hide();
        }

        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap();
    };

    public Footprint() {
        INSTANCE = this;
    }
}
