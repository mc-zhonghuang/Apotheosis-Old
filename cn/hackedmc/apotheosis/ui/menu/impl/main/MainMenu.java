package cn.hackedmc.apotheosis.ui.menu.impl.main;

import cn.hackedmc.apotheosis.util.MouseUtil;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.shader.RiseShaders;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.ui.menu.Menu;
import cn.hackedmc.apotheosis.ui.menu.component.button.MenuButton;
import cn.hackedmc.apotheosis.ui.menu.component.button.impl.MenuTextButton;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.io.IOException;

public final class MainMenu extends Menu {
    private final Font fontRenderer = FontManager.getProductSansBold(24);
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600);

    private MenuTextButton singlePlayerButton;
    private MenuTextButton multiPlayerButton;
    private MenuTextButton altManagerButton;
    private MenuTextButton settingButton;

    private MenuButton[] menuButtons;

    private boolean rice;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.singlePlayerButton == null || this.multiPlayerButton == null || this.altManagerButton == null|| this.settingButton == null) {
            return;
        }
        // Renders the background
        RiseShaders.MAIN_MENU_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, null);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        // Run blur
//        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
//        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_BLUR_RUNNABLES);
        // Run bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_POST_BLOOM_RUNNABLES);
        // Run post shader things
        InstanceAccess.clearRunnables();
        // Renders the buttons
        this.singlePlayerButton.draw(mouseX, mouseY, partialTicks);
        this.multiPlayerButton.draw(mouseX, mouseY, partialTicks);
        this.settingButton.draw(mouseX, mouseY, partialTicks);
        this.altManagerButton.draw(mouseX, mouseY, partialTicks);
        // Update the animation
        final double destination = this.singlePlayerButton.getY() - this.fontRenderer.height();
        this.animation.run(destination);
        // String name
        String name = rice ? "Ap0the0515" : Client.NAME;
        // Render the eo "logo"
        final double value = this.animation.getValue();
        this.fontRenderer.drawCenteredString(name, width / 2.0F, value, new Color(255,255,255).getRGB());
        // Draw bottom right text
        // TODO: Add the small "6.0"
        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.menuButtons == null) return;

        // If doing a left click and the mouse is hovered over a button, execute the buttons action (runnable)
        if (mouseButton == 0) {
            for (MenuButton menuButton : this.menuButtons) {
                if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    menuButton.runAction();
                    break;
                }
            }
        }
    }

    @Override
    public void initGui() {
        InstanceAccess.clearRunnables();
        rice = Math.random() > 0.95;
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int buttonWidth = 180;
        int buttonHeight = 24;
        int buttonSpacing = 6;
        int buttonX = centerX - buttonWidth / 2;
        int buttonY = centerY - buttonHeight / 2 - buttonSpacing / 2 - buttonHeight / 2;

        // Re-creates the buttons for not having to care about the animation reset
        this.singlePlayerButton = new MenuTextButton(buttonX, buttonY, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiSelectWorld(this)), "Single Player");
        this.multiPlayerButton = new MenuTextButton(buttonX, buttonY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiMultiplayer(this)), "Multi Player");
        this.altManagerButton = new MenuTextButton(buttonX, buttonY + buttonHeight * 2 + buttonSpacing * 2, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(Client.INSTANCE.getAltManagerMenu()), "Alts");
        this.settingButton = new MenuTextButton(buttonX, buttonY + buttonHeight * 3 + buttonSpacing * 3, buttonWidth, buttonHeight, () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)), "Settings");

        // Re-create the logo animation for not having to care about its reset
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600);

        // Putting all buttons in an array for handling mouse clicks
        this.menuButtons = new MenuButton[]{this.singlePlayerButton, this.multiPlayerButton, this.settingButton, this.altManagerButton};
    }
}
