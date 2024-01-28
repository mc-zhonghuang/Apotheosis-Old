package cn.hackedmc.alexander.module.impl.player.nofall;

import cn.hackedmc.alexander.component.impl.player.FallDistanceComponent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.module.impl.player.NoFall;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

/**
 * @author Strikeless
 * @since 13.03.2022
 */
public class InvalidNoFall extends Mode<NoFall> {

    public InvalidNoFall(String name, NoFall parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (mc.thePlayer.motionY > 0) {
            return;
        }

        float distance = FallDistanceComponent.distance;

        if (distance > 3) {
            final Block nextBlock = PlayerUtil.block(new BlockPos(event.getPosX(), event.getPosY() + mc.thePlayer.motionY, event.getPosZ()));

            if (nextBlock.getMaterial().isSolid()) {
                event.setPosY(event.getPosY() - 999);
                distance = 0;
            }
        }

        FallDistanceComponent.distance = distance;
    };
}