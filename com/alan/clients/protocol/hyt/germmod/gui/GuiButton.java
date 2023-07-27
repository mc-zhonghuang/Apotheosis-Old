package com.alan.clients.protocol.hyt.germmod.gui;

import com.alan.clients.protocol.hyt.germmod.PacketManager;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketCloseGui;
import com.alan.clients.protocol.hyt.germmod.packet.packets.PacketGuiState;
import com.alan.clients.protocol.hyt.germmod.util.GuiState;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.interfaces.InstanceAccess;

public class GuiButton implements InstanceAccess {
    private MenuTextButton button;
    private final String guiId;
    private final String buttonId;
    private final String text;
    private boolean hovered = false;

    public GuiButton(String text, String guiId, String buttonId) {
        this.text = text;
        this.buttonId = buttonId;
        this.guiId = guiId;
        this.button = new MenuTextButton(0, 0, 20, 20, this::sendClickButton, text);
    }

    public void drawButton(int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (MouseUtil.isHovered(x - 50, y - 10, 100, 20, mouseX, mouseY)) {
            this.button = new MenuTextButton(x - 50, y - 10, 100, 20, this::sendClickButton, this.text);
        }

        this.button.draw(mouseX, mouseY, partialTicks);

        if (mouseX > x - 50 && mouseX < x + 50 && mouseY > y - 10 && mouseY < y + 10) {
            if (!hovered) {
                PacketManager.sendPacket(new PacketGuiState(guiId, buttonId, GuiState.HOVERED));
            }
            hovered = true;
        } else {
            if (hovered) {
                PacketManager.sendPacket(new PacketGuiState(guiId, buttonId, GuiState.COVERED));
            }
            hovered = false;
        }
    }

    public void mouseClicked(int button) {
        if (this.button == null || button != 0) return;

        if (this.hovered) {
            this.button.runAction();
        }
    }

    private void sendClickButton() {
        PacketManager.sendPacket(new PacketGuiState(guiId, buttonId, GuiState.CLICK));
        PacketManager.sendPacket(new PacketCloseGui(guiId));
        mc.displayGuiScreen(null);
    }
}
