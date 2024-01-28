package cn.hackedmc.alexander.newevent.impl.render;

import cn.hackedmc.alexander.newevent.Event;
import cn.hackedmc.alexander.util.vector.Vector2f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class LookEvent implements Event {
    private Vector2f rotation;
}
