package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.ui.click.standard.components.ModuleComponent;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.impl.DragValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "module.combat.keybinds.name", description = "module.combat.keybinds.description", category = Category.RENDER)
public class KeyBinds extends Module {
    private Color logoColor;
    private final DragValue position = new DragValue("", this, new Vector2d(200, 100), true);
    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        double x = this.position.position.x;
        double y = this.position.position.y;
        logoColor = this.getTheme().getFirstColor();
        int index = 0;
        for (Module module : Client.INSTANCE.getModuleManager().getAll()) {
            if (module.getKeyCode() == Keyboard.KEY_NONE)
                continue;
            index++;
        }

    };
}