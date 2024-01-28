package cn.hackedmc.alexander.module.impl.player;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.module.impl.player.flagdetector.Flight;
import cn.hackedmc.alexander.module.impl.player.flagdetector.Friction;
import cn.hackedmc.alexander.module.impl.player.flagdetector.MathGround;
import cn.hackedmc.alexander.module.impl.player.flagdetector.Strafe;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.value.impl.BooleanValue;

@Rise
@ModuleInfo(name = "module.player.flagdetector.name", description = "module.player.flagdetector.description", category = Category.PLAYER)
public class FlagDetector extends Module {
    public BooleanValue strafe = new BooleanValue("Strafe (Watchdog)", this, false, new Strafe("", this));
    public BooleanValue friction = new BooleanValue("Friction", this, false, new Friction("", this));
    public BooleanValue flight = new BooleanValue("Flight", this, false, new Flight("", this));
    public BooleanValue mathGround = new BooleanValue("Math Ground", this, false, new MathGround("", this));

    @Override
    protected void onEnable() {
        if (!Client.DEVELOPMENT_SWITCH) {
            ChatUtil.display("This module is only enabled for developers or config makersconfig");

            toggle();
        }
    }

    public void fail(String check) {
        ChatUtil.display("§7failed " + Client.INSTANCE.getThemeManager().getTheme().getChatAccentColor() + check);
    }
}
