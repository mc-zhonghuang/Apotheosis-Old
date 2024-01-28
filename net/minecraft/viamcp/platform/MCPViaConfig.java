package net.minecraft.viamcp.platform;

import com.viaversion.viaversion.configuration.AbstractViaConfig;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MCPViaConfig extends AbstractViaConfig {
    private static final List<String> UNSUPPORTED = Arrays.asList("checkforupdates", "bungee-ping-interval", "bungee-ping-save", "bungee-servers",
            "velocity-ping-interval", "velocity-ping-save", "velocity-servers",
            "block-protocols", "block-disconnect-msg", "reload-disconnect-msg", "max-pps",
            "max-pps-kick-msg", "tracking-period", "tracking-warning-pps", "tracking-max-warnings", "tracking-max-kick-msg",
            "blockconnection-method", "quick-move-action-fix", "item-cache", "change-1_9-hitbox", "change-1_14-hitbox",
            "use-new-deathmessages", "nms-player-ticking");

    public MCPViaConfig(final File configFile) {
        super(configFile);
        this.reload();
    }

    @Override
    public URL getDefaultConfigURL() {
        return getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }

    @Override
    protected void handleConfig(final Map<String, Object> config) {
        // Is not needed!
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return UNSUPPORTED;
    }

    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }

    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }

    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }

    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }

    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }
}
