package cn.hackedmc.apotheosis.ui.menu.impl.main;

import cn.hackedmc.apotheosis.ui.menu.component.button.impl.MainMenuButton;
import cn.hackedmc.apotheosis.util.MouseUtil;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.shader.RiseShaders;
import cn.hackedmc.apotheosis.util.shader.base.RiseShader;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.ui.menu.Menu;
import cn.hackedmc.apotheosis.ui.menu.component.button.MenuButton;
import cn.hackedmc.apotheosis.ui.menu.component.button.impl.MenuTextButton;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.shader.impl.BloomShader;
import cn.hackedmc.apotheosis.util.shader.impl.GaussianBlurShader;
import cn.hackedmc.fucker.Fucker;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public final class MainMenu extends Menu {
    // "Logo" animation
    private final Font fontRenderer = FontManager.getProductSansRegular(64);
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600);

    private MainMenuButton singlePlayerButton;
    private MainMenuButton multiPlayerButton;
    private MainMenuButton altManagerButton;
    private MainMenuButton settingButton;
    private MainMenuButton exitButton;



    private MenuButton[] menuButtons;
    private Color leftColor;
    private Color rightColor;

    private RiseShader blurShader = new GaussianBlurShader(40);
    private RiseShader bloomShader = new BloomShader(20);


    private float y = 0;
    private float x = 0;

    private int count = 0;

    private int nextTick = 0;

    private long time;

    private void drawRain(){
        ScaledResolution sr = new ScaledResolution(mc);
        y-= sr.getScaledHeight() / 101f;
        x+= sr.getScaledWidth() / 427f;
        drawCustomImage(new ResourceLocation("apotheosis/images/rain.png"), 0, 0, sr.getScaledWidth(), sr.getScaledHeight());
    }

    public void drawCustomImage(final ResourceLocation imageLocation, final float x, final float y, final float width, final float heigh) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        mc.getTextureManager().bindTexture(imageLocation);
        drawModalRectWithCustomSizedTexture(x, y, this.x, this.y, width, height, width, height);
        GlStateManager.resetColor();
        GlStateManager.disableBlend();
    }

    private void drawThunder(){
        //重置
        if(count <= 0){
            count = new Random().nextInt(3) + 1;
            //3s-10s
            nextTick = new Random().nextInt(7000) + 3000;

            time = System.currentTimeMillis();
        }
        //假设闪电一个0.2s
        if(System.currentTimeMillis() - time > nextTick){
            //获取1s占比
            float per = (float) (System.currentTimeMillis() - time - nextTick) / 200 ;
            if(per>1){
                count--;
                nextTick += 200;
            }else{
                float percent = -4*per*per+4*per;
                RenderUtil.rectangle(0,0, Display.getWidth(),Display.getHeight(),new Color(255,255,255,(int)(255*percent)));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        leftColor = this.getTheme().getFirstColor();
        rightColor = this.getTheme().getSecondColor();
        //背景
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.image(new ResourceLocation("apotheosis/images/mainmenu.jpg"), 0,
                0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(255, 255, 255, 255));
        //下雨
        drawRain();
        //毛玻璃
        double rectWidth = 277;
        double rectHeight = 260.5f;
        //毛玻璃 blur
        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.roundedRectangle(width / 2 - rectWidth / 2,
                height / 2 - rectHeight / 2, rectWidth, rectHeight,2, Color.BLACK));
        blurShader.update();
        blurShader.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_BLUR_RUNNABLES);
        //top
        RenderUtil.drawRoundedGradientRect(width / 2 - rectWidth / 2, height / 2 - rectHeight / 2,
                rectWidth, 5,2,leftColor,rightColor, false);
        //毛玻璃 Outline
        RenderUtil.roundedRectangle(width / 2 - rectWidth / 2, height / 2 - rectHeight / 2,
                rectWidth, rectHeight,2, new Color(0,0,0,80));
        // 毛玻璃阴影
        NORMAL_POST_BLOOM_RUNNABLES.add(()->{
            RenderUtil.roundedRectangle(width / 2 - rectWidth / 2, height / 2 - rectHeight / 2,
                    rectWidth, rectHeight,2, Color.BLACK);
        });
        bloomShader.update();
        bloomShader.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_POST_BLOOM_RUNNABLES);
        //一些按钮
        this.singlePlayerButton.draw(mouseX,mouseY,partialTicks);
        this.multiPlayerButton.draw(mouseX,mouseY,partialTicks);
        this.altManagerButton.draw(mouseX,mouseY,partialTicks);
        this.settingButton.draw(mouseX,mouseY,partialTicks);
        this.exitButton.draw(mouseX,mouseY,partialTicks);
        //文字
        this.fontRenderer.drawCenteredString(Client.NAME, width / 2.0F, this.singlePlayerButton.getY() - this.fontRenderer.height() - 17, new Color(255,255,255,200).getRGB());
        //闪电,要全局 所以放在这里
        drawThunder();
        //收尾工作
        InstanceAccess.clearRunnables();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.menuButtons == null) return;
        // If doing a left click and the mouse is hovered over a button, execute the buttons action (runnable)
        if (mouseButton == 0) {
            for (MenuButton menuButton : this.menuButtons) {
                if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                    menuButton.runAction();
                    break;
                }
            }
        }

    }

    @Override
    public void initGui() {
        long totalMilliSeconds = System.currentTimeMillis();
        Date nowTime = new Date(totalMilliSeconds);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        String retStrFormatNowDate = sdf3.format(nowTime);
        //Reset
        InstanceAccess.clearRunnables();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int buttonWidth = 180;
        int buttonHeight = 24;
        int buttonSpacing = 6;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = centerY - buttonHeight / 2 - buttonSpacing / 2 - buttonHeight / 2 - 20;
        this.singlePlayerButton = new MainMenuButton(buttonX, buttonY, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiSelectWorld(this)), "Singleplayer");
        this.multiPlayerButton = new MainMenuButton(buttonX, buttonY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiMultiplayer(this)), "Multiplayer");
        this.altManagerButton = new MainMenuButton(buttonX, buttonY + buttonHeight * 2 + buttonSpacing * 2, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(Client.INSTANCE.getAltManagerMenu()), "Alts");
        this.settingButton = new MainMenuButton(buttonX, buttonY + buttonHeight * 3 + buttonSpacing * 3, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiOptions(this,mc.gameSettings)), "Settings");
        this.exitButton = new MainMenuButton(buttonX, buttonY + buttonHeight * 4 + buttonSpacing * 4, buttonWidth, buttonHeight, () -> System.exit(0), "Exit");
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600);
        this.menuButtons = new MenuButton[]{this.singlePlayerButton, this.multiPlayerButton, this.altManagerButton,this.settingButton,this.exitButton};
        time = System.currentTimeMillis();
        Display.setTitle(Client.NAME + " " + Client.VERSION + " | " + "User: " + Fucker.name + " | " + retStrFormatNowDate);
    }
}
