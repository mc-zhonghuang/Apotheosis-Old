package cn.hackedmc.alexander.component.impl.packetlog;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.ServerJoinEvent;
import cn.hackedmc.alexander.newevent.impl.other.WorldChangeEvent;

@Rise
public class PacketLogComponent extends Component {

    private int worldChanges;

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        worldChanges++;
    };

    @EventLink()
    public final Listener<ServerJoinEvent> onServerJoin = event -> {
        worldChanges = 0;
    };

    public boolean hasChangedWorlds() {
        return worldChanges > 0;
    }
}