package cn.hackedmc.apotheosis.newevent;

import cn.hackedmc.apotheosis.script.api.wrapper.impl.event.ScriptEvent;

public interface Event {
    public default ScriptEvent<? extends Event> getScriptEvent() {
        return null;
    }
}
