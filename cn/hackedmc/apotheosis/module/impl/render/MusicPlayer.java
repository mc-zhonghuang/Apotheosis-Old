package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import org.lwjgl.input.Keyboard;

@Rise
@ModuleInfo(name = "module.render.musicplayer.name", description = "module.render.musicplayer.description", category = Category.RENDER)
public class MusicPlayer extends Module {
//    @Override
//    public void onEnable() {
//        getModule(ClickGUI.class).setEnabled(false);
//        Client.INSTANCE.getEventBus().register(Client.INSTANCE.getMusicMenu());
//        mc.displayGuiScreen(Client.INSTANCE.getMusicMenu());
//    }
//
//    @Override
//    protected void onDisable() {
//        Client.INSTANCE.getEventBus().unregister(Client.INSTANCE.getMusicMenu());
//        mc.displayGuiScreen(null);
//    }
}
