package cn.hackedmc.apotheosis.ui.click.standard.components.theme;

import cn.hackedmc.apotheosis.ui.theme.Themes;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector3d;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Getter
@RequiredArgsConstructor
public class ThemeKeyColorComponent implements InstanceAccess {
    private final Themes.KeyColors color;

    private Vector3d lastDraw = new Vector3d(0, 0, 0);
    private final Animation dimAnimation = new Animation(Easing.EASE_OUT_QUINT, 500);
    private final Animation bloomAnimation = new Animation(Easing.EASE_OUT_QUINT, 500);

    public void draw(double x, double y, double width, boolean selected) {
        double value = dimAnimation.getValue();

        RenderUtil.roundedRectangle(x, y, width, 17, 5, new Color(18, 21, 30));
        RenderUtil.roundedRectangle(x + 0.5, y + 0.5, width - 1, 16, 4, color.getColor());

        RenderUtil.roundedRectangle(x, y, width, 17, 5, new Color(25, 25, 25,
                (int) ((1 - dimAnimation.getValue()) * 128)));

        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(x, y, width, 17, 5, new Color(18, 21, 30,
                    (int) (bloomAnimation.getValue() * 255)));
            RenderUtil.roundedRectangle(x + 0.5, y + 0.5, width - 1, 16, 4,
                    ColorUtil.withAlpha(color.getColor(), (int) (bloomAnimation.getValue() * 255)));
        });

        this.lastDraw = new Vector3d(x, y, width);
    }
}