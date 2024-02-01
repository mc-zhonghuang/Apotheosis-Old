package cn.hackedmc.apotheosis.newevent.impl.motion;

import cn.hackedmc.apotheosis.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LavaEvent implements Event {
    private boolean lava;
}
