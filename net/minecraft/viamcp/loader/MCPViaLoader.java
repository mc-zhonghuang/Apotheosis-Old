package net.minecraft.viamcp.loader;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import net.minecraft.viamcp.ViaMCP;

public class MCPViaLoader implements ViaPlatformLoader {
    @Override
    public void load() {
        final ViaProviders providers = Via.getManager().getProviders();
//        providers.use(MovementTransmitterProvider.class, new BukkitViaMovementTransmitter());
        providers.use(VersionProvider.class, new BaseVersionProvider() {
            @Override
            public int getClosestServerProtocol(final UserConnection connection) throws Exception {
                if (connection.isClientSide()) {
                    return ViaMCP.getInstance().getVersion();
                }

                return super.getClosestServerProtocol(connection);
            }
        });
    }

    @Override
    public void unload() {
    }
}
