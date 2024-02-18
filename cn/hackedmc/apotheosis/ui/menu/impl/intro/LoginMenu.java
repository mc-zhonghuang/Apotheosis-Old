package cn.hackedmc.apotheosis.ui.menu.impl.intro;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.ui.menu.Menu;
import cn.hackedmc.apotheosis.ui.menu.component.button.MenuButton;
import cn.hackedmc.apotheosis.ui.menu.component.button.impl.MenuTextButton;
import cn.hackedmc.apotheosis.ui.menu.impl.main.MainMenu;
import cn.hackedmc.apotheosis.util.ByteUtil;
import cn.hackedmc.apotheosis.util.CryptUtil;
import cn.hackedmc.apotheosis.util.MouseUtil;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.gui.textbox.TextAlign;
import cn.hackedmc.apotheosis.util.gui.textbox.TextBox;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.shader.RiseShaders;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.util.vantage.HWIDUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.fucker.Fucker;
import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;

public class LoginMenu extends Menu {
    private final Font fontRenderer = FontManager.getProductSansBold(24);
    private Animation animation = new Animation(Easing.EASE_OUT_QUINT, 600);
    private MenuTextButton nameButton;
    private MenuTextButton loginButton;
    private TextBox nameBox;
    private MenuButton[] menuButtons;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RiseShaders.MAIN_MENU_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, null);

        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, InstanceAccess.NORMAL_POST_BLOOM_RUNNABLES);

        InstanceAccess.clearRunnables();

        this.nameButton.draw(mouseX, mouseY, partialTicks);
        this.loginButton.draw(mouseX, mouseY, partialTicks);

        this.nameBox.draw();

        final double destination = this.nameButton.getY() - this.fontRenderer.height();
        this.animation.run(destination);

        final double value = this.animation.getValue();
        this.fontRenderer.drawCenteredString(Client.NAME, width / 2.0F, value, new Color(255,255,255).getRGB());

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();

        if (Fucker.login) mc.displayGuiScreen(new MainMenu());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.menuButtons == null) return;

        if (mouseButton == 0) {
            for (MenuButton menuButton : this.menuButtons) {
                if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.getWidth(), menuButton.getHeight(), mouseX, mouseY)) {
                    menuButton.runAction();
                    break;
                }
            }

            this.nameBox.click(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.nameBox.key(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_TAB) {
            this.nameBox.setSelected(!this.nameBox.isSelected());
        }

        else if (keyCode == Keyboard.KEY_RETURN && !this.nameBox.getText().isEmpty()) {
            this.loginButton.runAction();
        }
    }

    @Override
    public void initGui() {
        Display.setTitle("Waiting for login");

        InstanceAccess.clearRunnables();

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int buttonWidth = 180;
        int buttonHeight = 24;

        this.nameButton = new MenuTextButton(centerX - buttonWidth / 2.0, centerY - buttonHeight / 2.0, buttonWidth, buttonHeight, () -> {}, "");
        this.loginButton = new MenuTextButton(centerX - buttonWidth / 2.0, centerY + buttonHeight * 4, buttonWidth, buttonHeight, () -> {
            if (Fucker.channel != null) {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Name", this.nameBox.getText());
                jsonObject.addProperty("UUID", HWIDUtil.getUUID());
                final JsonObject command = new JsonObject();
                command.addProperty("LittleDogWhatAreYouDoing", CryptUtil.Base64Crypt.encrypt(jsonObject.toString()));

                Fucker.name = this.nameBox.getText();
                ByteUtil.send(Fucker.channel, CryptUtil.DES.encrypt(command.toString(), Fucker.username, Fucker.password));
            }
        }, "Login");
        this.nameBox = new TextBox(new Vector2d(centerX, centerY - buttonHeight / 2.0 + 9), FontManager.getProductSansBold(24), Color.WHITE, TextAlign.CENTER, "Username", buttonWidth * 5);

        menuButtons = new MenuButton[]{this.nameButton, this.loginButton};
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600);
        this.animation.reset();
    }
}
