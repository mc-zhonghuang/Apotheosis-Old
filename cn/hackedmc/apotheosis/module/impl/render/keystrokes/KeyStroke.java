package cn.hackedmc.apotheosis.module.impl.render.keystrokes;

import cn.hackedmc.apotheosis.ui.theme.Themes;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@RequiredArgsConstructor
public class KeyStroke implements InstanceAccess {
    private final Vector2f scale;

    private final Vector2f offset;

    private final String name;

    private final KeyBinding binding;

    private final Animation animation = new Animation(Easing.LINEAR, 200);
    private float heldMultiplier = 0.0F;

    public KeyStroke(final Vector2f offset, final String name, final KeyBinding binding) {
        this(new Vector2f(RenderUtil.GENERIC_SCALE, RenderUtil.GENERIC_SCALE), offset, name, binding);
    }

    public KeyStroke(final Vector2f offset, final KeyBinding binding) {
        this(offset, Keyboard.getKeyName(binding.getKeyCode()), binding);
    }

    public void render(Vector2d position) {
        position = new Vector2d(
                position.getX() + offset.getX(),
                position.getY() + offset.getY()
        );

        RenderUtil.roundedRectangle(
                position.getX(), position.getY(),
                scale.getX(), scale.getY(),
                5, ColorUtil.withAlpha(getTheme().getBackgroundShade(), (int) animation.getValue())
        );

        this.updateHeld();

        final Vector2d textSize = new Vector2d(nunitoNormal.width(name), nunitoNormal.height());
        final Vector2d textPosition = new Vector2d(
                position.getX() + (scale.getX() - textSize.getX()) / 2.0F - 0.5F,
                position.getY() + (scale.getY() - textSize.getY()) / 2.0F + 3.0F
        );

        final Themes theme = this.getTheme();
        final Color color = ColorUtil.mixColors(
                Color.WHITE,
                theme.getSecondColor(),
                heldMultiplier
        );

        nunitoNormal.drawStringWithShadow(name, textPosition.getX(), textPosition.getY(), color.getRGB());
    }

    public void bloom(final Vector2d position) {
        RenderUtil.roundedRectangle(position.x + offset.x, position.y + offset.y, scale.x, scale.y, 6, new Color(0, 0, 0, 150));
    }

    public void blur(final Vector2d position) {
        RenderUtil.roundedRectangle(position.x + offset.x, position.y + offset.y, scale.x, scale.y, 4, Color.BLACK);
    }

    public void updateHeld() {
        int alpha = getTheme().getBackgroundShade().getAlpha();
        animation.run(binding.isKeyDown() ? Math.min(alpha * 1.7f, 255) : alpha);
    }
}
