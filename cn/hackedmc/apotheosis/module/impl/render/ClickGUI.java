package cn.hackedmc.apotheosis.module.impl.render;


import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.input.KeyboardInputEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * Displays a GUI which can display and do various things
 *
 * @author Alan
 * @since 04/11/2021
 */
@ModuleInfo(name = "module.render.clickgui.name", description = "module.render.clickgui.description", category = Category.RENDER, keyBind = Keyboard.KEY_RSHIFT)
public final class ClickGUI extends Module {
    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(Client.INSTANCE.getStandardClickGUI());
        mc.displayGuiScreen(Client.INSTANCE.getStandardClickGUI());
//        Client.INSTANCE.getStandardClickGUI().overlayPresent = null;
    }

    @Override
    public void onDisable() {
        Keyboard.enableRepeatEvents(false);
        Client.INSTANCE.getEventBus().unregister(Client.INSTANCE.getStandardClickGUI());
        Client.INSTANCE.getExecutor().execute(() -> Client.INSTANCE.getConfigFile().write());
        this.mc.displayGuiScreen(null);
    }

    @EventLink(value = Priorities.HIGH)
    public final Listener<Render2DEvent> onRender2D = event -> {
        double width = event.getScaledResolution().getScaledWidth();
        double height = event.getScaledResolution().getScaledHeight();

        UI_RENDER_RUNNABLES.add(() -> Client.INSTANCE.getStandardClickGUI().render());
        UI_BLOOM_RUNNABLES.add(() -> Client.INSTANCE.getStandardClickGUI().bloom());
        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.rectangle(0, 0, width, height, Color.BLACK));

        if (this.mc.currentScreen == null) {
            this.setEnabled(false);
        }
    };

    @EventLink()
    public final Listener<KeyboardInputEvent> onKey = event -> {
        if (event.getKeyCode() == this.getKeyCode()) {
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    };
}
