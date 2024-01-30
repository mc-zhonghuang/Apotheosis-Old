package cn.hackedmc.apotheosis.module.impl.movement.noslow;

import cn.hackedmc.apotheosis.component.impl.player.SlotComponent;
import cn.hackedmc.apotheosis.module.impl.movement.NoSlow;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PostMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.component.impl.hypixel.InventoryDeSyncComponent;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class OldIntaveNoSlow extends Mode<NoSlow> {

    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = event -> {
//        InventoryDeSyncComponent.setActive("/booster");

        if (mc.thePlayer.isUsingItem()) {
            PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex() % 8 + 1));
            PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex()));
        }
    };

    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = event -> {
        if (mc.thePlayer.isUsingItem() && InventoryDeSyncComponent.isDeSynced()) {
            PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
        }
    };

    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = event -> {
        event.setCancelled(true);
    };

    public OldIntaveNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }
}