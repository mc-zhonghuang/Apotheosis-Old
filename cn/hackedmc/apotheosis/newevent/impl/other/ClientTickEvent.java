package cn.hackedmc.apotheosis.newevent.impl.other;

import cn.hackedmc.apotheosis.newevent.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientTickEvent implements Event {
    public enum Type {
        PRE,
        POST
    }

    private final Type type;

    public ClientTickEvent(Type type) {
        this.type = type;
    }
}
