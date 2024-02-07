package cn.hackedmc.apotheosis.module.impl.render.footprint;

import cn.hackedmc.apotheosis.module.impl.render.Footprint;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render3DEvent;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDepthMask;

public class Body extends Mode<Footprint> {
    public Body(String name, Footprint parent) {
        super(name, parent);
    }
    private List<PlayerRenderData> renderData = new ArrayList<>();

    @Override
    public void onEnable() {
        renderData.clear();
    }

    @EventLink
    private Listener<WorldChangeEvent> onWorldChange = event -> {
        renderData.clear();
    };

    @EventLink
    private Listener<PreUpdateEvent> onPreUpdate = event -> {
        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || (player.prevPosX == player.posX && player.prevPosY == player.posY && player.prevPosZ == player.posZ && player.prevRotationYaw == player.rotationYaw && player.prevRotationPitch == player.rotationPitch && player.prevRotationYawHead == player.rotationYawHead)) continue;
            final EntityOtherPlayerMP playerMP = new EntityOtherPlayerMP(mc.theWorld, player.gameProfile);
            playerMP.renderNameTag = false;
            playerMP.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
            playerMP.rotationYawHead = player.rotationYawHead;
            playerMP.moveForward = player.moveForward;
            playerMP.moveStrafing = player.moveStrafing;
            playerMP.setSneaking(player.isSneaking());
            playerMP.setSprinting(player.isSprinting());
            playerMP.setEating(player.isEating());
            playerMP.prevPosX = player.prevPosX;
            playerMP.prevPosY = player.prevPosY;
            playerMP.prevPosZ = player.prevPosZ;
            playerMP.prevRotationYaw = player.prevRotationYaw;
            playerMP.prevRotationPitch = player.prevRotationPitch;
            playerMP.prevRotationYawHead = player.prevRotationYawHead;
            playerMP.limbSwing = player.limbSwing;
            playerMP.limbSwingAmount = player.limbSwingAmount;
            playerMP.prevLimbSwingAmount = player.prevLimbSwingAmount;

            renderData.add(new PlayerRenderData(playerMP, mc.getRenderManager().getEntityRenderObject(playerMP), 0));
        }

        renderData = renderData.stream().map(PlayerRenderData::build).filter(data -> data.tick < getParent().amount.getValue().intValue()).collect(Collectors.toList());
    };

    @EventLink
    private Listener<Render3DEvent> onRender3D = event -> {
        final float partialTicks = event.getPartialTicks();
        for (final PlayerRenderData data : renderData) {
            final Entity player = data.getEntity();
            final Render<Entity> render = data.getRender();

            if (mc.getRenderManager() == null || render == null) {
                continue;
            }

            final Color color = ColorUtil.withAlpha(ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getSecondColor(), data.getTick() / getParent().amount.getValue().doubleValue()), (int) (255.0 * (1 - (data.getTick() / getParent().amount.getValue().doubleValue()))));

            if (color.getAlpha() <= 0) {
                continue;
            }

            final double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            final double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
            final float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL11.GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
            RendererLivingEntity.setShaderBrightnessWithAlpha(color);
            render.doRender(player, x - mc.getRenderManager().renderPosX, y - mc.getRenderManager().renderPosY, z - mc.getRenderManager().renderPosZ, yaw, partialTicks);
            RendererLivingEntity.unsetShaderBrightness();
            GlStateManager.color(1F, 1F, 1F, 1F);
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL11.GL_TEXTURE_2D);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();

            player.hide();
        }

        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap();
    };
}
