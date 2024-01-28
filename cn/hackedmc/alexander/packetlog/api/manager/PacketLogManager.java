package cn.hackedmc.alexander.packetlog.api.manager;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.packetlog.Check;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;

import java.util.ArrayList;

/**
 * @author Alan
 * @since 10/19/2021
 */
public final class PacketLogManager extends ArrayList<Check> implements InstanceAccess {
    public boolean packetLogging;

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
        });
    };
}