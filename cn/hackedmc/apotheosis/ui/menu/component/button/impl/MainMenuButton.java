package cn.hackedmc.apotheosis.ui.menu.component.button.impl;

import cn.hackedmc.apotheosis.ui.menu.component.button.MenuButton;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.shader.base.RiseShader;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.util.shader.impl.BloomShader;
import cn.hackedmc.apotheosis.util.shader.impl.GaussianBlurShader;

import java.awt.*;


public class MainMenuButton  extends MenuButton {
    private static final Font FONT_RENDERER = FontManager.getProductSansBold(25);

    public String name;

    private RiseShader blurShader = new GaussianBlurShader(40);
    private RiseShader bloomShader = new BloomShader(20);
    public MainMenuButton(double x, double y, double width, double height, Runnable runnable, String name) {
        super(x, y, width, height, runnable);
        this.name = name;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // Runs the animation update - keep this
        super.draw(mouseX, mouseY, partialTicks);

        final double value = getY();
        final double progress = value / this.getY();
        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.roundedRectangle(this.getX(), value, this.getWidth(), this.getHeight(),
                2,new Color(0,0,0,50)));
        NORMAL_POST_BLOOM_RUNNABLES.add(()->  RenderUtil.roundedRectangle(this.getX(), value, this.getWidth(), this.getHeight(),
                2,new Color(0,0,0,50)));

        RenderUtil.roundedRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 2,new Color(0,0,0,50));

       // blurShader.update();
      //  blurShader.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_BLUR_RUNNABLES);
        //RenderUtil.roundedOutlineRectangle(this.getX(), value, this.getWidth(), this.getHeight(), 0, 1, basic);
        FONT_RENDERER.drawCenteredString(this.name, (float) (this.getX() + this.getWidth() / 2.0F),
                (float) (value + this.getHeight() / 2.0F - 4),new Color(255,255,255,200).getRGB());

    }
}
