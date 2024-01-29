package cn.hackedmc.apotheosis.module.impl.movement.inventorymove;

import cn.hackedmc.apotheosis.module.impl.movement.InventoryMove;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.JumpEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Auth
 */
public class BufferAbuseInventoryMove extends Mode<InventoryMove> {

    private final NumberValue clicksSetting = new NumberValue("Clicks", this, 3, 2, 10, 1);
    private final NumberValue amount = new NumberValue("Amount", this, 5, 1, 10, 1);

    private final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();

    private boolean done, sent;
    private int clicks;

    public BufferAbuseInventoryMove(String name, InventoryMove parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (this.canAbuse()) {
            if (!this.sent) {
                if (!this.done) {
                    this.done = true;
                } else {
                    for (int i = 0; i < this.amount.getValue().intValue(); i++) {
                        PacketUtil.sendNoEvent(new C0EPacketClickWindow());
                    }
                    packets.forEach(PacketUtil::sendNoEvent);
                    packets.clear();
                    this.sent = true;
                }
            }
        } else {
            this.done = false;
            this.sent = false;
        }
    };

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {
        if (this.canAbuse() && !this.sent) {
            event.setCancelled(true);
        }
    };

    @EventLink()
    public final Listener<JumpEvent> onJump = event -> {
        if (this.canAbuse() && !this.sent) {
            event.setCancelled(true);
        }
    };

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {

        final Packet<?> p = event.getPacket();

        if (p instanceof C0EPacketClickWindow) {
            if (this.canAbuse() && !this.sent) {
                event.setCancelled(true);
                packets.add(p);
                return;
            }
            this.clicks++;
        }
    };

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        packets.clear();
    };

    private boolean canAbuse() {
        return clicks > 0 && clicks % this.clicksSetting.getValue().intValue() == 0;
    }

    private final KeyBinding[] AFFECTED_BINDINGS = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump
    };

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen == this.getStandardClickGUI()) {
            return;
        }

        for (final KeyBinding bind : AFFECTED_BINDINGS) {
            bind.setPressed(GameSettings.isKeyDown(bind));
        }
    };
}