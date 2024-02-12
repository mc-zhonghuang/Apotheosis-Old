package cn.hackedmc.apotheosis.ui.music.screen;

import cn.hackedmc.apotheosis.ui.music.screen.impl.BasicScreen;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.render.RenderUtil;

import java.awt.*;

public class MusicBasic extends BasicScreen {
    private final String[] buttonName = new String[]{"搜索", "已下载", "播放列表"};
    private final Font font = FontManager.getNunito(40);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final float buttonX = 100;
        final float buttonY = 60;

        final float basicX = getPosition().x - 100;
        final float basicY = getPosition().y + getScale().y;
        for (int index = 1; index <= 3;index++) {
            final int finalIndex = index;
            Runnable button = () -> RenderUtil.roundedRectangle(basicX, basicY - buttonY * finalIndex, buttonX, buttonY, 2, new Color(47, 47, 47, 152));
            button.run();
            font.drawCenteredString(buttonName[3 - index], basicX + buttonX / 2, basicY - buttonY * index + buttonY / 2 - font.height() / 2, new Color(166, 166, 166).getRGB(), true);
            UI_BLOOM_RUNNABLES.add(button);
            UI_POST_BLOOM_RUNNABLES.add(button);
        }
    }
}
