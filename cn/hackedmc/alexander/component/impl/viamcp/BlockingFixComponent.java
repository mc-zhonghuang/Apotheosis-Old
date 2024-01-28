package cn.hackedmc.alexander.component.impl.viamcp;

import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.module.impl.movement.NoSlow;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import com.viaversion.viarewind.protocol.protocol1_8to1_9.Protocol1_8To1_9;
import com.viaversion.viarewind.utils.PacketUtil;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Type;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.viamcp.ViaMCP;

public class BlockingFixComponent extends Component {
    private NoSlow noSlow;

    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement) {
            final C08PacketPlayerBlockPlacement wrapped = (C08PacketPlayerBlockPlacement) packet;

            if (wrapped.getPosition().equals(new BlockPos(-1, -1, -1)) && wrapped.getPlacedBlockDirection() == 255 && ViaMCP.getInstance().getVersion() > ProtocolVersion.v1_8.getVersion()) {
                if (noSlow == null) noSlow = getModule(NoSlow.class);

                if (!noSlow.mode.getValue().getName().equalsIgnoreCase("HuaYuTing")) {
                    PacketWrapper useItem = PacketWrapper.create(29, null, Via.getManager().getConnectionManager().getConnections().iterator().next());
                    useItem.write(Type.VAR_INT, 1);
                    PacketUtil.sendToServer(useItem, Protocol1_8To1_9.class, true, true);
                }
            }
        }
    };
}
