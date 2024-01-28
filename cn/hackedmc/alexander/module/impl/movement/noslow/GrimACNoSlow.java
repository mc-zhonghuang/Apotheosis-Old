package cn.hackedmc.alexander.module.impl.movement.noslow;

import cn.hackedmc.alexander.module.impl.combat.KillAura;
import cn.hackedmc.alexander.module.impl.movement.NoSlow;
import cn.hackedmc.alexander.newevent.CancellableEvent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class GrimACNoSlow extends Mode<NoSlow> {
    public GrimACNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (mc.thePlayer.isUsingItem() || this.getModule(KillAura.class).blocking) {
            PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
            PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = CancellableEvent::setCancelled;
}
