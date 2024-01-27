package com.alan.clients.module.impl.movement.inventorymove;

import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.value.Mode;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.MovementInput;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author LvZiQiao
 * @since 25.1.2024
 */

public class WatchdogInventoryMove extends Mode<InventoryMove> {

    private int chestCloseTicks;
    private int chestId;
    private final LinkedBlockingQueue<C03PacketPlayer> c03s = new LinkedBlockingQueue<>();

    public WatchdogInventoryMove(String name, InventoryMove parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {

        final Packet<?> packet = event.getPacket();

         if (packet instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement = ((C08PacketPlayerBlockPlacement) packet);

            if (PlayerUtil.block(c08PacketPlayerBlockPlacement.getPosition()) instanceof BlockChest) {
                chestCloseTicks = -1;
            }
        } else if (packet instanceof C0DPacketCloseWindow) {
             if (((C0DPacketCloseWindow) packet).windowId != 0) {
                chestCloseTicks = 0;
                event.setCancelled();
                chestId = ((C0DPacketCloseWindow) packet).windowId;
            }
        } else if (packet instanceof C0EPacketClickWindow) {
             chestCloseTicks = -1;
        } else if (packet instanceof C03PacketPlayer) {
            if (chestCloseTicks < 3 && chestCloseTicks != -1) {
                event.setCancelled();
                c03s.add((C03PacketPlayer) packet);
                if (chestCloseTicks == 2) {
                    PacketUtil.sendNoEvent(new C0DPacketCloseWindow(chestId));
                }
                chestCloseTicks++;
            } else {
                c03s.forEach(PacketUtil::sendNoEvent);
                c03s.clear();
                chestCloseTicks = -1;
            }
        }
    };

    @EventLink
    public final Listener<WorldChangeEvent> onWorld = event -> {
        c03s.clear();
        chestCloseTicks = -1;
    };

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