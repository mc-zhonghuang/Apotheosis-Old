package cn.hackedmc.apotheosis.module.impl.movement.noslow;

import cn.hackedmc.apotheosis.component.impl.render.NotificationComponent;
import cn.hackedmc.apotheosis.module.impl.combat.KillAura;
import cn.hackedmc.apotheosis.module.impl.movement.NoSlow;
import cn.hackedmc.apotheosis.newevent.CancellableEvent;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PostMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.SlowDownEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.value.Mode;
import com.viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viarewind.utils.PacketUtil;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.viamcp.ViaMCP;

import java.util.concurrent.LinkedBlockingQueue;

public class GrimACNoSlow extends Mode<NoSlow> {
    public GrimACNoSlow(String name, NoSlow parent) {
        super(name, parent);
    }

    private boolean blinking = false;
    private final LinkedBlockingQueue<Packet<?>> packets = new LinkedBlockingQueue<>();

    @EventLink()
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.getHeldItem() != null) {
            if ((mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) && mc.thePlayer.isUsingItem()) {
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
                mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MadeByLvZiQiao", new PacketBuffer(Unpooled.buffer())));
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }
    };

    @EventLink()
    private final Listener<PostMotionEvent> onPostMotion = event -> {
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.isUsingItem()) {
            mc.getNetHandler().addToSendQueueUnregistered(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            PacketWrapper useItem = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
            useItem.write(Type.VAR_INT, 1);
            PacketUtil.sendToServer(useItem, Protocol1_8To1_9.class, true, true);
        }
    };

    @EventLink()
    private final Listener<SlowDownEvent> onSlowDown = CancellableEvent::setCancelled;

    @EventLink()
    private final Listener<WorldChangeEvent> onWorld = event -> {
        blinking = false;
        packets.clear();
    };

    @EventLink()
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (mc.thePlayer == null || mc.isSingleplayer()) return;

        if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && !ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())))) {
            if (packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement) packet).getPosition().equals(new BlockPos(-1, -1, -1))) {
                blinking = true;

                if (ViaMCP.getInstance().getVersion() <= 47)
                    NotificationComponent.post("No Slow", "This NoSlow only supports higher versions!", 10000);
            } else if (!(packet instanceof C00Handshake || packet instanceof C00PacketLoginStart ||
                    packet instanceof C00PacketServerQuery || packet instanceof C01PacketPing ||
                    packet instanceof C01PacketEncryptionResponse) && blinking) {
                event.setCancelled();

                if (packet instanceof C07PacketPlayerDigging) {
                    final C07PacketPlayerDigging wrapped = (C07PacketPlayerDigging) packet;

                    if (wrapped.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                        blinking = false;
                        mc.getNetHandler().addToSendQueueUnregistered(wrapped);
                        packets.forEach(p -> mc.getNetHandler().addToSendQueueUnregistered(p));
                        packets.clear();
                        return;
                    }
                }

                packets.add(packet);
            }
        } else if (blinking) {
            blinking = false;
            mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            packets.forEach(p -> mc.getNetHandler().addToSendQueueUnregistered(p));
            packets.clear();
        }
    };
}
