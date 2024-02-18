package cn.hackedmc.apotheosis.module.impl.render.targetinfo;

import cn.hackedmc.apotheosis.module.impl.render.TargetInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.TickEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.render.StencilUtil;
import cn.hackedmc.apotheosis.util.render.particle.Particle;
import cn.hackedmc.apotheosis.component.impl.render.ParticleComponent;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

public class ShineTargetInfo extends Mode<TargetInfo> {

    private final BooleanValue particles = new BooleanValue("Particles", this, true);
    private final Font productSansMedium = FontManager.getProductSansRegular(22);
    private final Font productSansS = FontManager.getProductSansRegular(16);

    private final ModeValue backgroundMode = new ModeValue("Background Mode", this) {{
        add(new SubMode("Glass"));
        add(new SubMode("Tint"));
        //        add(new SubMode("Solid")); looks ass
        setDefault("Glass");
    }};
    private DecimalFormat format = new DecimalFormat("0.0");
    private DecimalFormat format0 = new DecimalFormat("0");

    private TargetInfo targetInfoModule;
    private int EDGE_OFFSET = 8, PADDING = 7, INDENT = 4;

    private Animation openingAnimation = new Animation(Easing.EASE_OUT_ELASTIC, 500);
    private Animation healthAnimation = new Animation(Easing.EASE_OUT_SINE, 500);

    public ShineTargetInfo(String name, TargetInfo parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {

        if (this.targetInfoModule == null) {
            this.targetInfoModule = this.getModule(TargetInfo.class);
        }

        Entity target = this.targetInfoModule.target;
        if (target == null) return;

        boolean out = (!this.targetInfoModule.inWorld || this.targetInfoModule.stopwatch.finished(1000));
        openingAnimation.setDuration(out ? 400 : 850);
        openingAnimation.setEasing(out ? Easing.EASE_IN_BACK : Easing.EASE_OUT_ELASTIC);
        openingAnimation.run(out ? 0 : 1);

        if (openingAnimation.getValue() <= 0) return;

        String name = target.getCommandSenderName();

        double x = this.targetInfoModule.position.x;
        double y = this.targetInfoModule.position.y;

        double nameWidth = productSansMedium.width(name);
        double health = Math.min(!this.targetInfoModule.inWorld ? 0 : MathUtil.round(((AbstractClientPlayer) target).getHealth(), 1), ((AbstractClientPlayer) target).getMaxHealth());
        double healthTextWidth = productSansMedium.width(String.valueOf(health));
        double healthBarWidth = Math.max(nameWidth + 35 - healthTextWidth, 65);

        healthAnimation.run((health / ((AbstractClientPlayer) target).getMaxHealth()) * healthBarWidth);
        healthAnimation.setEasing(Easing.EASE_OUT_QUINT);
        healthAnimation.setDuration(250);
        double healthRemainingWidth = healthAnimation.getValue();
        double hurtTime = (((AbstractClientPlayer) target).hurtTime == 0 ? 0 :
                ((AbstractClientPlayer) target).hurtTime - mc.timer.renderPartialTicks) * 0.5;
        int faceScale = 32;
        double faceOffset = hurtTime / 2f;
        double width = EDGE_OFFSET + faceScale + EDGE_OFFSET + healthBarWidth + INDENT + healthTextWidth + EDGE_OFFSET;
        double height = faceScale + EDGE_OFFSET * 2;
        this.targetInfoModule.positionValue.setScale(new Vector2d(width, height));

        double scale = openingAnimation.getValue();

        NORMAL_POST_RENDER_RUNNABLES.add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            // Draw background
            Color background1 = new Color(0, 0, 0, 100);
            Color background2 = new Color(0, 0, 0, 100);
            Color accent1 = getTheme().getFirstColor();
            Color accent2 = getTheme().getSecondColor();
            Color logoColor = this.getTheme().getFirstColor();

            if (this.backgroundMode.getValue().getName().equals("Tint")) {
                Color theme1 = this.getTheme().getAccentColor(new Vector2d(x, y)), theme2 = this.getTheme().getAccentColor(new Vector2d(x, y + height));
                background1 = new Color(theme1.getRed() / 5, theme1.getGreen() / 5, theme1.getBlue() / 5, 128);
                background2 = new Color(theme2.getRed() / 5, theme2.getGreen() / 5, theme2.getBlue() / 5, 128);
            } else if (this.backgroundMode.getValue().getName().equals("Solid")) {
                Color theme1 = this.getTheme().getFirstColor(), theme2 = this.getTheme().getSecondColor();
                background1 = new Color(theme1.getRed(), theme1.getGreen(), theme1.getBlue(), 128);
                background2 = new Color(theme2.getRed(), theme2.getGreen(), theme2.getBlue(), 128);
                accent1 = new Color(255, 255, 255);
                accent2 = new Color(164, 164, 164);
            }
            RenderUtil.drawRoundedGradientRect(x + 4, y, width - 30, height - 5, 0, background1, background2, true);

            RenderUtil.drawRoundedGradientRect(x + 4, y, width - 30, height - 43, 0, accent2, logoColor, true);
            RenderUtil.drawRoundedGradientRect(x + 4, y, width - 30, height - 43, 0, logoColor, accent2, true);
            // Name
            productSansS.drawString("Name: " + ((AbstractClientPlayer) target).getName(), x + 44, y + 13, new Color(-1).getRGB());
            GlStateManager.color(1, 1, 1, 1);
            // Targets face
            RenderUtil.color(ColorUtil.mixColors(Color.RED, Color.WHITE, hurtTime / 9));
            RenderUtil.dropShadow(3, x + EDGE_OFFSET + faceOffset, y + EDGE_OFFSET + faceOffset,
                    faceScale - hurtTime, faceScale - hurtTime, 20, this.getTheme().getRound() * 2);
            renderTargetHead((AbstractClientPlayer) target, x + EDGE_OFFSET + faceOffset, y + EDGE_OFFSET + faceOffset,
                    faceScale - hurtTime);

            // Health background
            RenderUtil.roundedRectangle(x + EDGE_OFFSET + faceScale + PADDING - 3, y + EDGE_OFFSET + faceScale - INDENT - 12, healthBarWidth,
                    12, 0, getTheme().getBackgroundShade());

            RenderUtil.drawRoundedGradientRect(x + EDGE_OFFSET + faceScale + PADDING - 3, y + EDGE_OFFSET + faceScale - INDENT - 12,
                    healthRemainingWidth, 12, 0, accent2, accent1, true);

            GlStateManager.popMatrix();
        });

        NORMAL_PRE_RENDER_RUNNABLES.add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            // 头像
            RenderUtil.roundedRectangle(x + EDGE_OFFSET + faceOffset, y + EDGE_OFFSET + faceOffset,
                    faceScale - hurtTime, faceScale - hurtTime, this.getTheme().getRound() * 2,
                    ColorUtil.withAlpha(Color.RED, (int) (hurtTime / 9 * 255)));
            // 血条
            RenderUtil.drawRoundedGradientRect(x + EDGE_OFFSET + faceScale + PADDING - 3, y + EDGE_OFFSET + faceScale - INDENT - 12, healthRemainingWidth, 12, 0, getTheme().getFirstColor(), getTheme().getSecondColor(), true);

            GlStateManager.popMatrix();
        });

        NORMAL_BLUR_RUNNABLES.add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);
            RenderUtil.roundedRectangle(x + 4, y, width - 30, height - 5, 0, Color.BLACK);
            GlStateManager.popMatrix();
        });

        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            GlStateManager.pushMatrix();
            GlStateManager.translate((x + width / 2) * (1 - scale), (y + height / 2) * (1 - scale), 0);
            GlStateManager.scale(scale, scale, 0);

            final boolean glow = false;
            final Color outlineColor1 = glow ? this.getTheme().getFirstColor() : new Color(0, 0, 0, 128);
            final Color outlineColor2 = glow ? this.getTheme().getSecondColor() : new Color(0, 0, 0, 128);
            RenderUtil.drawRoundedGradientRect(x + 4, y, width - 30, height - 5, 0, outlineColor1, outlineColor2, true);

            GlStateManager.popMatrix();
        });
    };

    private void renderTargetHead(final AbstractClientPlayer abstractClientPlayer, final double x, final double y, final double size) {
        StencilUtil.initStencil();
        StencilUtil.bindWriteStencilBuffer();
        RenderUtil.roundedRectangle(x, y, size, size, this.getTheme().getRound() * 2, this.getTheme().getBackgroundShade());
        StencilUtil.bindReadStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.enableTexture2D();

        final ResourceLocation resourceLocation = targetInfoModule.inWorld && abstractClientPlayer.getHealth() > 0
                ? abstractClientPlayer.getLocationSkin() : RenderSkeleton.getEntityTexture();

        mc.getTextureManager().bindTexture(resourceLocation);

        Gui.drawScaledCustomSizeModalRect(x, y, 4, 4, 4, 4, size, size, 32, 32);
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
    }

    @EventLink()
    public final Listener<TickEvent> onTick = event -> {

        if (this.targetInfoModule == null) return;
        Entity target = this.targetInfoModule.target;

        if (target == null || openingAnimation.getValue() <= 0 || !this.particles.getValue()) return;

        double hurtTime = (((AbstractClientPlayer) target).hurtTime == 0 ? 0 :
                ((AbstractClientPlayer) target).hurtTime - mc.timer.renderPartialTicks) * 0.5;

        if (hurtTime > 0) {
            for (int i = 0; i < hurtTime * Math.random() / 2; i++) {
                ParticleComponent.add(new Particle(new Vector2f((float) (targetInfoModule.position.x + 20), (float) (targetInfoModule.position.y + 20)),
                        new Vector2f((float) (Math.random() - 0.5) * 1.7f, (float) (Math.random() - 0.5) * 1.7f)));
            }
        }
    };
}
