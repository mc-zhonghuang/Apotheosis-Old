package cn.hackedmc.alexander.newevent;

import cn.hackedmc.alexander.script.api.wrapper.impl.event.ScriptEvent;

public interface Event {
    public default ScriptEvent<? extends Event> getScriptEvent() {
        return null;
    }
}
