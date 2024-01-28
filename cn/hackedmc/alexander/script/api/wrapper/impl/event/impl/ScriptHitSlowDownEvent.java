package cn.hackedmc.alexander.script.api.wrapper.impl.event.impl;

import cn.hackedmc.alexander.newevent.impl.motion.HitSlowDownEvent;
import cn.hackedmc.alexander.script.api.wrapper.impl.event.ScriptEvent;

/**
 * @author Auth
 * @since 9/07/2022
 */
public class ScriptHitSlowDownEvent extends ScriptEvent<HitSlowDownEvent> {

    public ScriptHitSlowDownEvent(final HitSlowDownEvent wrappedEvent) {
        super(wrappedEvent);
    }

    public void setSlowDown(final double slowDown) {
        this.wrapped.setSlowDown(slowDown);
    }

    public void setSprint(final boolean sprint) {
        this.wrapped.setSprint(sprint);
    }

    public double getSlowDown() {
        return this.wrapped.getSlowDown();
    }

    public boolean isSprint() {
        return this.wrapped.isSprint();
    }

    @Override
    public String getHandlerName() {
        return "onHitSlowDown";
    }
}
