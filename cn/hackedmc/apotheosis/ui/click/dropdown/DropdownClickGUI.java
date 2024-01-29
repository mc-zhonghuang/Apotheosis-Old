package cn.hackedmc.apotheosis.ui.click.dropdown;

import cn.hackedmc.apotheosis.ui.click.dropdown.components.CategoryComponent;
import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.impl.render.ClickGUI;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DropdownClickGUI extends GuiScreen {

    private final List<CategoryComponent> categoryComponents = new ArrayList<>();

    @Getter
    private final Color mainColor = new Color(30, 30, 30);

    public DropdownClickGUI() {
        double posX = 20;

        for (final Category category : Category.values()) {
            switch (category) {
                case SEARCH:
                    continue;
            }

            this.categoryComponents.add(new CategoryComponent(category, posX));
            posX += 110;
        }
    }

    public void render() {
        for (final CategoryComponent category : categoryComponents) {
            category.render();
        }
    }

    public void bloom() {
        categoryComponents.forEach(CategoryComponent::bloom);
    }

    @Override
    public void onGuiClosed() {
        Client.INSTANCE.getModuleManager().get(ClickGUI.class).toggle();
    }
}
