package cn.hackedmc.apotheosis.newevent.impl.motion;

import cn.hackedmc.apotheosis.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alan
 * @since 13.03.2022
 */
@Getter
@Setter
@AllArgsConstructor
public class WaterEvent implements Event {
    private boolean water;
}
