package cn.hackedmc.apotheosis.packetlog.api.manager;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.packetlog.Check;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Alan
 * @since 10/19/2021
 */
public final class PacketLogManager extends ArrayList<Check> implements InstanceAccess {
    public boolean packetLogging = true;

    public void init() {
        Client.INSTANCE.getEventBus().register(this);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.ticksExisted % 20 != 0) return;

        threadPool.execute(() -> {
            boolean detected = false;

            for (Check check : this) {
                if (check.run()) {
                    detected = true;
                    break;
                }
            }

            packetLogging = detected;

            if (!packetLogging) {
                try {
                    Minecraft.theMinecraft.fontRendererObj = null;
                    Minecraft.theMinecraft.gameSettings = null;
                    Minecraft.theMinecraft.ingameGUI = null;
                    Minecraft.theMinecraft.shutdown();
                    Minecraft.theMinecraft = null;
                    System.exit(0);
                } catch (Exception ignored) {}
            }
        });
    };

    public void addAll(Class<? extends Check> ... checks) {
        Arrays.asList(checks).forEach(clazz -> {
            try {
                this.add(clazz.newInstance());
            } catch (Exception ignored) {}
        });
    }
}