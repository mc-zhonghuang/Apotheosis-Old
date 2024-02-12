package cn.hackedmc.apotheosis.ui.music;

import cn.hackedmc.apotheosis.ui.menu.Menu;
import cn.hackedmc.apotheosis.ui.music.screen.MusicBasic;
import cn.hackedmc.apotheosis.util.gui.GUIUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2f;

import java.awt.*;
import java.io.IOException;

public class MusicMenu extends Menu {
    public Vector2f position = new Vector2f(-1, -1);
    public Vector2f scale = new Vector2f(400, 260);
    public Vector2f draggingOffset = new Vector2f(-1, -1);
    public boolean dragging;
    public final MusicBasic basicGui = new MusicBasic();

    @Override
    public void initGui() {
        dragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            position.x = mouseX - draggingOffset.x;
            position.y = mouseY - draggingOffset.y;
        }

        Runnable background = () -> {
            RenderUtil.roundedRectangle(position.x, position.y, scale.x, scale.y, 3, new Color(138, 138, 138, 153));
        };

        background.run();

        UI_BLOOM_RUNNABLES.add(background);
        UI_POST_BLOOM_RUNNABLES.add(background);
        NORMAL_BLUR_RUNNABLES.add(background);

        basicGui.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (GUIUtil.mouseOver(position.x, position.y, scale.x, scale.y, mouseX, mouseY) && !dragging) {
            draggingOffset.x = mouseX - position.x;
            draggingOffset.y = mouseY - position.y;
            dragging = true;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    @Override
    public void onGuiClosed() {
        dragging = false;
    }
}
