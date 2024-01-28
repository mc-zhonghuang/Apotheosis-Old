package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author Auth
 * @since 3/02/2022
 */
public class PacketNoFall extends Mode<NoFall> {

    public PacketNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        float distance = FallDistanceComponent.distance;

        if (distance > 3) {
            PacketUtil.send(new C03PacketPlayer(true));
            distance = 0;
        }

        FallDistanceComponent.distance = distance;
    };
}