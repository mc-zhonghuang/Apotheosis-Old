package cn.hackedmc.apotheosis.value.impl;

import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.Value;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author Alan
 * @since 10/19/2021
 */
@Setter
public class DragValue extends Value<Vector2d> {

    public Vector2d position = new Vector2d(100, 100), targetPosition = new Vector2d(100, 100), scale = new Vector2d(100, 100), lastScale = new Vector2d(-1, -1);
    public Animation animationPosition = new Animation(Easing.LINEAR, 600), smoothAnimation = new Animation(Easing.EASE_OUT_EXPO, 300);
    public ScaledResolution lastScaledResolution = new ScaledResolution(InstanceAccess.mc);
    public boolean render = true, structure;

    public DragValue(final String name, final Module parent, final Vector2d defaultValue) {
        super(name, parent, defaultValue);
    }

    public DragValue(final String name, final Module parent, final Vector2d defaultValue, final boolean render) {
        super(name, parent, defaultValue);
        this.render = render;
    }

    public DragValue(final String name, final Module parent, final Vector2d defaultValue, final boolean render, final boolean structure) {
        super(name, parent, defaultValue);
        this.render = render && !structure;
        this.structure = structure;
    }

    public DragValue(final String name, final Mode<?> parent, final Vector2d defaultValue) {
        super(name, parent, defaultValue);
    }

    public DragValue(final String name, final Module parent, final Vector2d defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    public DragValue(final String name, final Mode<?> parent, final Vector2d defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    @Override
    public List<Value<?>> getSubValues() {
        return null;
    }

    public void setScale(Vector2d scale) {
        this.scale = scale;
        if (lastScale.x == -1 && lastScale.y == -1) {
            this.lastScale = this.scale;
        }

        ScaledResolution scaledResolution = InstanceAccess.mc.scaledResolution;

        if (this.position.x > scaledResolution.getScaledWidth() / 2f) {
            this.targetPosition.x += this.lastScale.x - this.scale.x;
            this.position = targetPosition;
        }

        if (this.position.y > scaledResolution.getScaledHeight() / 2f) {
            this.targetPosition.y += this.lastScale.y - this.scale.y;
            this.position = targetPosition;
        }

//        if (lastScaledResolution != null) {
//            this.position.x /= ((double) lastScaledResolution.getScaledWidth() / scaledResolution.getScaledWidth()) / scaledResolution.getScaleFactor();
//            this.position.y /= ((double) lastScaledResolution.getScaledHeight() / scaledResolution.getScaledHeight()) / scaledResolution.getScaleFactor();
//        }

        this.lastScale = scale;
        this.lastScaledResolution = scaledResolution;
    }
}