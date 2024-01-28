package cn.hackedmc.alexander.ui.click.clover.button.impl;

import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.render.RenderUtil;
import cn.hackedmc.alexander.ui.click.clover.button.api.Button;
import cn.hackedmc.alexander.util.vector.Vector2d;

import java.awt.*;

public class OptionsButton extends Button implements InstanceAccess {
    public String name;
    public boolean selected;

    //Dimensions
    public double selectedHeight = 1;

    public OptionsButton(Vector2d position, Vector2d scale, Runnable click, String name) {
        super(position, scale, click);

        this.name = name;
        this.selected = false;
    }

    @Override
    public void render() {
        super.render();

        nunitoSmall.drawString(name, position.x + scale.x / 2 - nunitoSmall.width(name) / 2f, position.y + scale.y / 2 - nunitoSmall.height() / 2f + 4, (selected ? Color.WHITE : getCloverClickGUI().deSelected).hashCode());

        if (selected) {
            RenderUtil.rectangle(position.x, position.y + scale.y - selectedHeight, scale.x, selectedHeight, Color.WHITE);
        }
    }
}
