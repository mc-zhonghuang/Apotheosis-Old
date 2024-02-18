package cn.hackedmc.apotheosis.module.impl.render.ui;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.render.Gui;
import cn.hackedmc.apotheosis.module.impl.render.Interface;
import cn.hackedmc.apotheosis.module.impl.render.TargetInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.ServerUtils;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.font.impl.minecraft.FontRenderer;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.DragValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class Logo extends Mode<Gui> {
    private Gui guiModule;
    private Color logoColor;

    public Logo(String name, Gui parent) {
        super(name, parent);
    }
    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        if (this.guiModule == null) {
            this.guiModule = this.getModule(Gui.class);
        }

        String text = " | " + Client.VERSION + " | " + mc.thePlayer.getCommandSenderName() + " | " + ServerUtils.getRemoteIp() + " | " + "Fps:"+ Minecraft.getDebugFPS();
        int sb = 6 * 25 - 5;
        int left = FontManager.getProductSansRegular(20).width(text);
        guiModule.positionValue.scale = new Vector2d(200, 100);
        // Don't draw if the F3 menu is open
        if (mc.gameSettings.showDebugInfo) return;
        logoColor = this.getTheme().getFirstColor();

        Color color1 = ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getFirstColor(), getTheme().getBlendFactor(new Vector2d(0, guiModule.position.y)));
        Color color2 = ColorUtil.mixColors(getTheme().getFirstColor(), getTheme().getFirstColor(), getTheme().getBlendFactor(new Vector2d(0, guiModule.position.y + guiModule.positionValue.scale.y * 8.75)));
        //background
      //  RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 80, 0, new Color(0,0,0,100));
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(guiModule.position.x + 10, guiModule.position.y + 10, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 80, 0, new Color(0,0,0,100));
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color1,color2,true);
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color2,color1,true);
        });
        NORMAL_RENDER_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(guiModule.position.x + 10, guiModule.position.y + 10, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 80, 0, new Color(0,0,0,100));
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color1,color2,true);
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color2,color1,true);
        });

        NORMAL_BLUR_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(guiModule.position.x + 10, guiModule.position.y + 10, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 80, 0, new Color(0,0,0,100));
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color1,color2,true);
            RenderUtil.drawRoundedGradientRect(guiModule.position.x + 10, guiModule.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color2,color1,true);
        });
        //font
        FontManager.getProductSansRegular(20).drawStringWithShadow(Client.NAME, guiModule.position.x + 13, guiModule.position.y + 17, logoColor.getRGB());
        FontManager.getProductSansRegular(20).drawStringWithShadow(text, guiModule.position.x + 67, guiModule.position.y + 17, -1);
        //two
      //  RenderUtil.drawRoundedGradientRect(position.position.x + 10, position.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color1,color2,true);
      //  RenderUtil.drawRoundedGradientRect(position.position.x + 10, position.position.y + 8, left + guiModule.positionValue.scale.x - sb + 5, guiModule.positionValue.scale.y - 96, 0, color2,color1,true);

    };
}
