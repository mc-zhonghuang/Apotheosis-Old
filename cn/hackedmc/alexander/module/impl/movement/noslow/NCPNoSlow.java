package cn.hackedmc.alexander.module.impl.movement.noslow;

import cn.hackedmc.alexander.component.impl.player.SlotComponent;
import cn.hackedmc.alexander.module.impl.movement.NoSlow;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PostMotionEvent;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author Auth
 * @since 18/11/2021
 */

public class NCPNoSlow extends Mode<NoSlow> {
    public NCPNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (InstanceAccess.mc.thePlayer.isBlocking()) {
            PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.UP.getIndex(), null, 0.0F, 0.0F, 0.0F));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        event.setCancelled(true);
    };
}