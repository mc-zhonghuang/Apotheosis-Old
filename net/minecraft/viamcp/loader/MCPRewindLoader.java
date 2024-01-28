package net.minecraft.viamcp.loader;

import com.viaversion.viarewind.api.ViaRewindPlatform;
import com.viaversion.viaversion.api.Via;

import java.io.File;
import java.util.logging.Logger;

public class MCPRewindLoader implements ViaRewindPlatform {
    public MCPRewindLoader(final File file) {
        this.init(file.toPath().resolve("ViaRewind").resolve("config.yml").toFile());
    }

    @Override
    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }
}
