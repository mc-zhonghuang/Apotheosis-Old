package cn.hackedmc.apotheosis.ui.click.clover.setting.impl;

import cn.hackedmc.apotheosis.ui.click.clover.setting.api.SettingComp;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.value.Value;

import java.awt.*;

public class BooleanSettingComponent extends SettingComp implements InstanceAccess {
    public BooleanSettingComponent(Value value) {
        super(value);
    }

    @Override
    public void render() {
        FontManager.getNunito(20).drawString(value.getName(), position.x, position.y, Color.WHITE.hashCode());

        RenderUtil.roundedRectangle(position.x, position.y, 20, 8, 4, ColorUtil.withAlpha(Color.WHITE, 100));
    }
}
