package cn.hackedmc.apotheosis.script.api.wrapper.impl.event.impl;


import cn.hackedmc.apotheosis.newevent.impl.input.KeyboardInputEvent;
import cn.hackedmc.apotheosis.script.api.wrapper.impl.event.ScriptEvent;

/**
 * @author Auth
 * @since 9/07/2022
 */
public class ScriptKeyboardInputEvent extends ScriptEvent<KeyboardInputEvent> {

    public ScriptKeyboardInputEvent(final KeyboardInputEvent wrappedEvent) {
        super(wrappedEvent);
    }

    public int getKeyCode() {
        return this.wrapped.getKeyCode();
    }

    @Override
    public String getHandlerName() {
        return "onKeyboardInput";
    }
}
