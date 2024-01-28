package cn.hackedmc.alexander.newevent.impl.other;

import cn.hackedmc.alexander.newevent.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public final class ServerKickEvent implements Event {
    public List<String> message;
}