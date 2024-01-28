package cn.hackedmc.alexander.ui.click.clover.setting.impl;

import cn.hackedmc.alexander.ui.click.clover.setting.api.SettingComp;
import cn.hackedmc.alexander.util.font.FontManager;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.render.ColorUtil;
import cn.hackedmc.alexander.util.render.RenderUtil;
import cn.hackedmc.alexander.value.Value;

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
