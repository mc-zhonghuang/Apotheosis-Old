package cn.hackedmc.alexander.module.impl.ghost.autoclicker;

import cn.hackedmc.alexander.module.impl.ghost.AutoClicker;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.value.Mode;
import cn.hackedmc.alexander.value.impl.BoundsNumberValue;

public class DragClickAutoClicker extends Mode<AutoClicker> {
    private final BoundsNumberValue length = new BoundsNumberValue("Drag Click Length", this, 17, 18, 1, 50, 1);
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay Between Dragging", this, 6, 6, 1, 20, 1);

    private int nextLength, nextDelay;

    public DragClickAutoClicker(String name, AutoClicker parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<TickEvent> onTick = event -> {

        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (nextLength < 0) {
                nextDelay--;

                if (nextDelay < 0) {
                    nextDelay = delay.getRandomBetween().intValue();
                    nextLength = length.getRandomBetween().intValue();
                }
            } else if (Math.random() < 0.95) {
                nextLength--;
                PlayerUtil.sendClick(0, true);
            }
        }
    };
}
