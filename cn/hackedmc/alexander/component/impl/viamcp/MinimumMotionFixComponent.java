package cn.hackedmc.alexander.component.impl.viamcp;

import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.MinimumMotionEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.viamcp.ViaMCP;

public final class MinimumMotionFixComponent extends Component {

    @EventLink()
    public final Listener<MinimumMotionEvent> onMinimumMotion = event -> {
        if (ViaMCP.getInstance().getVersion() > ProtocolVersion.v1_8.getVersion()) {
            event.setMinimumMotion(0.003D);
        }
    };
}
