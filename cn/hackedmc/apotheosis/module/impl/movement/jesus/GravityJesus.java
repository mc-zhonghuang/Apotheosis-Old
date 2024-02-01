package cn.hackedmc.apotheosis.module.impl.movement.jesus;

import cn.hackedmc.apotheosis.module.impl.movement.Jesus;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.WaterEvent;
import cn.hackedmc.apotheosis.value.Mode;
import util.time.StopWatch;

/**
 * @author Alan
 * @since 16.05.2022
 */

public class GravityJesus extends Mode<Jesus> {

    public GravityJesus(String name, Jesus parent) {
        super(name, parent);
    }
    private final StopWatch stopWatch = new StopWatch();

    @Override
    public void onEnable() {
        stopWatch.reset();
    }

    @EventLink()
    public final Listener<WaterEvent> onWater = event -> {
        if (stopWatch.finished(100)) {
            event.setWater(event.isWater() && mc.thePlayer.offGroundTicks > 5 && mc.gameSettings.keyBindJump.isKeyDown());
            stopWatch.reset();
        }
    };
}