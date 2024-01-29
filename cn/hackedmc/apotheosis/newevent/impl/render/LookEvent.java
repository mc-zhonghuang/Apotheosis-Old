package cn.hackedmc.apotheosis.newevent.impl.render;

import cn.hackedmc.apotheosis.newevent.Event;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class LookEvent implements Event {
    private Vector2f rotation;
}
