package cn.hackedmc.apotheosis.ui.click.dropdown.components;

import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.ui.click.dropdown.DropdownClickGUI;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import lombok.Getter;

@Getter
public class CategoryComponent implements InstanceAccess {

    private final Category category;
    private final double posX;

    private final boolean expanded = true;
    private boolean selected;

    public CategoryComponent(final Category category, final double posX) {
        this.category = category;
        this.posX = posX;
    }

    public void render() {
        final DropdownClickGUI dropdownClickGUI = this.getDropdownClickGUI();

        RenderUtil.roundedRectangle(posX, 20, 90, 20, 5, dropdownClickGUI.getMainColor());
        RenderUtil.rectangle(posX, 25, 90, 15, dropdownClickGUI.getMainColor());
        this.nunitoNormal.drawString(category.getName(), (float) (posX + 4), 26.5F, -1);
    }

    public void bloom() {

    }
}
