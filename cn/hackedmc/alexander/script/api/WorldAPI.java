package cn.hackedmc.alexander.script.api;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.script.api.wrapper.impl.ScriptWorld;

/**
 * @author Strikeless
 * @since 20.06.2022
 */
public class WorldAPI extends ScriptWorld {

    public WorldAPI() {
        super(MC.theWorld);

        Client.INSTANCE.getEventBus().register(this);
    }

    @EventLink()
    public final Listener<TickEvent> onTick = event -> {
        if (this.wrapped == null) {
            this.wrapped = MC.theWorld;
        }
    };
}
