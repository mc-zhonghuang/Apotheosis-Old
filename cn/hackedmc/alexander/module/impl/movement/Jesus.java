package cn.hackedmc.alexander.module.impl.movement;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.impl.movement.jesus.GravityJesus;
import cn.hackedmc.alexander.module.impl.movement.jesus.KarhuJesus;
import cn.hackedmc.alexander.module.impl.movement.jesus.NCPJesus;
import cn.hackedmc.alexander.module.impl.movement.jesus.VanillaJesus;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.JumpEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.ModeValue;

@Rise
@ModuleInfo(name = "module.movement.jesus.name", description = "module.movement.jesus.description", category = Category.MOVEMENT)
public class Jesus extends Module {

    public final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaJesus("Vanilla", this))
            .add(new GravityJesus("Gravity", this))
            .add(new KarhuJesus("Karhu", this))
            .add(new NCPJesus("NCP", this))
            .setDefault("Vanilla");

    private final BooleanValue allowJump = new BooleanValue("Allow Jump", this, true);

    @EventLink()
    public final Listener<JumpEvent> onJump = event -> {

        if (!allowJump.getValue() && PlayerUtil.onLiquid()) {
            event.setCancelled(true);
        }
    };
}