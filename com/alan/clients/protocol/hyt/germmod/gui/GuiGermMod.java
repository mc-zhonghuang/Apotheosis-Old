package com.alan.clients.protocol.hyt.germmod.gui;

import com.alan.clients.protocol.hyt.germmod.PacketManager;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketCloseGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public class GuiGermMod extends GuiScreen {
    private final String guiId;
    private final ArrayList<GuiButton> buttons;

    public GuiGermMod(String guiId, ArrayList<GuiButton> buttons) {
        this.guiId = guiId;
        this.buttons = buttons;
        if (buttons.size() == 0) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int y = height / 2 - 20;
        for (GuiButton button : buttons) {
            button.drawButton(width / 2, y, mouseX, mouseY, partialTicks);
            y += 40;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiButton button : buttons) {
            button.mouseClicked(mouseButton);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            PacketManager.sendPacket(new PacketCloseGui(guiId));
        }
        super.keyTyped(typedChar, keyCode);
    }
}
