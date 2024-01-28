package net.minecraft.client.gui;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.ui.menu.impl.main.MainMenu;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiDownloadTerrain extends GuiScreen {
    private final NetHandlerPlayClient netHandlerPlayClient;
    private int progress;
    private final CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    public GuiDownloadTerrain(final NetHandlerPlayClient netHandler) {
        this.netHandlerPlayClient = netHandler;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.leaveServer();
            mc.displayGuiScreen(new GuiMultiplayer(new MainMenu()));
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.buttonList.clear();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        ++this.progress;

        if (this.progress % 20 == 0) {
            this.netHandlerPlayClient.addToSendQueue(new C00PacketKeepAlive());
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.customLoadingScreen != null) {
            this.customLoadingScreen.drawBackground(this.width, this.height);
        } else {
            this.drawBackground(0);
        }

        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Press ESC to leave this menu", this.width / 2, this.height / 2 - 25, 16777215);
        this.drawCenteredString(this.fontRendererObj, "or press RETURN to force yourself into the server (features by " + Client.NAME + ")", this.width / 2, this.height / 2 - 12, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }
}
