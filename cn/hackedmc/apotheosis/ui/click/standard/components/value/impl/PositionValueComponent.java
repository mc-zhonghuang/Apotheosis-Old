package cn.hackedmc.apotheosis.ui.click.standard.components.value.impl;

import cn.hackedmc.apotheosis.ui.click.standard.components.value.ValueComponent;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.Value;
import net.minecraft.util.ResourceLocation;

public class PositionValueComponent extends ValueComponent {

    private final ResourceLocation image = new ResourceLocation("apotheosis/icons/click.png");

    public PositionValueComponent(final Value<?> value) {
        super(value);
    }

    @Override
    public void draw(final Vector2d position, final int mouseX, final int mouseY, final float partialTicks) {
        this.height = 0;
    }

    @Override
    public void click(final int mouseX, final int mouseY, final int mouseButton) {
    }

    @Override
    public void released() {
    }

    @Override
    public void bloom() {
    }

    @Override
    public void key(final char typedChar, final int keyCode) {

    }
}
