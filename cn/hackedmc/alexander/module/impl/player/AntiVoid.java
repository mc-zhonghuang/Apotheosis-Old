package cn.hackedmc.alexander.module.impl.player;


import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.module.impl.player.antivoid.*;
import cn.hackedmc.alexander.module.impl.player.antivoid.*;
import cn.hackedmc.alexander.value.impl.ModeValue;


/**
 * @author Alan
 * @since 23/10/2021
 */

@Rise
@ModuleInfo(name = "module.player.antivoid.name", description = "module.player.antivoid.description", category = Category.PLAYER)
public class AntiVoid extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new PacketAntiVoid("Packet", this))
            .add(new PositionAntiVoid("Position", this))
            .add(new BlinkAntiVoid("Blink", this))
            .add(new BlinkAntiVoid("Watchdog", this))
            .add(new VulcanAntiVoid("Vulcan", this))
            .add(new CollisionAntiVoid("Collision", this))
            .setDefault("Packet");
}