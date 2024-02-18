package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.exploit.disabler.*;
import cn.hackedmc.apotheosis.module.impl.render.ui.*;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.Value;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.DragValue;

@Rise
@ModuleInfo(name = "module.combat.gui.name", description = "module.combat.gui.description", category = Category.RENDER)
public class Gui extends Module {
    public static Gui INSTANCE;
    public final DragValue positionValue = new DragValue("Position", this, new Vector2d(200, 200));

    public Vector2d position = new Vector2d(0, 0);
    private final BooleanValue health =
            new BooleanValue("Health", this, false, new Health("", this));

    private final BooleanValue logo =
            new BooleanValue("Logo", this, false, new Logo("", this));

    private final BooleanValue keyBinds =
            new BooleanValue("KeyBinds", this, false, new KeyBinds("", this));

    private final BooleanValue sessionInfo =
            new BooleanValue("SessionInfo", this, false, new SessionInfo("", this));

    private final BooleanValue inventory =
            new BooleanValue("Inventory", this, false, new Inventory("", this));

    public Gui() {
        INSTANCE = this;
    }
}