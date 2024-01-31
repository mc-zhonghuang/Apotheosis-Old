package cn.hackedmc.apotheosis.module.impl.movement.inventorymove;

import cn.hackedmc.apotheosis.module.impl.movement.InventoryMove;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author LvZiQiao
 * @since 25.1.2024
 */

public class WatchdogInventoryMove extends Mode<InventoryMove> {

    public WatchdogInventoryMove(String name, InventoryMove parent) {
        super(name, parent);
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

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen == this.getStandardClickGUI() || mc.currentScreen instanceof GuiChest) {
            return;
        }

        for (final KeyBinding bind : AFFECTED_BINDINGS) {
            bind.setPressed(GameSettings.isKeyDown(bind));
        }
    };
}