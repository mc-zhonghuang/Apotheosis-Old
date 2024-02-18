package cn.hackedmc.apotheosis.module.impl.render.ui;

import cn.hackedmc.apotheosis.module.impl.render.Gui;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Health extends Mode<Gui> {
    public Health(String name, Gui parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final float maxHealth = mc.thePlayer.getMaxHealth();
        for (int n = 0; n < maxHealth / 2.0f; ++n) {
            mc.ingameGUI.drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 + 1f - maxHealth / 2.0f * 10.0f / 2.0f + n * 10, (scaledResolution.getScaledHeight() / 2 - 20 + 30), 16, 0, 9, 9);
        }
        final float health = mc.thePlayer.getHealth();
        for (int n2 = 0; n2 < health / 2.0f; ++n2) {
            mc.ingameGUI.drawTexturedModalRect(scaledResolution.getScaledWidth() / 2 + 1f - maxHealth / 2.0f * 10.0f / 2.0f + n2 * 10, (scaledResolution.getScaledHeight() / 2 - 20 + 30), 52, 0, 9, 9);
        }
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
    };
}
