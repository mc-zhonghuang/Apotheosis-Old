package cn.hackedmc.apotheosis.value.impl;

import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.Value;

import java.awt.*;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * @author Alan
 * @since 10/19/2022
 */
public class ColorValue extends Value<Color> {

    public ColorValue(final String name, final Module parent, final Color defaultValue) {
        super(name, parent, defaultValue);
    }

    public ColorValue(final String name, final Mode<?> parent, final Color defaultValue) {
        super(name, parent, defaultValue);
    }

    public ColorValue(final String name, final Module parent, final Color defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    public ColorValue(final String name, final Mode<?> parent, final Color defaultValue, final BooleanSupplier hideIf) {
        super(name, parent, defaultValue, hideIf);
    }

    @Override
    public List<Value<?>> getSubValues() {
        return null;
    }
}