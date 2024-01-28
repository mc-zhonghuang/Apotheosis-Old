package net.minecraft.viamcp.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.configuration.ViaVersionConfig;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.UnsupportedSoftware;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.VersionInfo;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.viamcp.ViaMCP;
import net.minecraft.viamcp.utils.JLoggerToLog4j;
import net.minecraft.viamcp.utils.TaskUtil;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MCPViaPlatform implements ViaPlatform<UUID> {
    private final Logger logger = new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));

    private final MCPViaConfig config;
    private final File dataFolder;
    private final ViaAPI<UUID> api;

    public MCPViaPlatform(final File dataFolder) {
        final Path configDir = dataFolder.toPath().resolve("ViaVersion");
        config = new MCPViaConfig(configDir.resolve("viaversion.yml").toFile());
        config.reload();
        this.dataFolder = configDir.toFile();
        api = new MCPViaAPI();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String getPlatformName() {
        return "ViaMCP";
    }

    @Override
    public String getPlatformVersion() {
        return String.valueOf(ViaMCP.PROTOCOL_VERSION);
    }

    @Override
    public boolean isProxy() {
        return ViaPlatform.super.isProxy();
    }

    @Override
    public String getPluginVersion() {
        return VersionInfo.getVersion();
    }

    @Override
    public TaskUtil runAsync(Runnable runnable) {
        return new TaskUtil(Via.getManager().getScheduler().execute(runnable));
    }

    @Override
    public TaskUtil runRepeatingAsync(Runnable runnable, long period) {
        return new TaskUtil(Via.getManager().getScheduler().scheduleRepeating(runnable, 0, period * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public TaskUtil runSync(Runnable runnable) {
        return this.runAsync(runnable);
    }

    @Override
    public TaskUtil runSync(Runnable runnable, long delay) {
        return new TaskUtil(Via.getManager().getScheduler().schedule(runnable, delay * 50, TimeUnit.MILLISECONDS));
    }

    @Override
    public TaskUtil runRepeatingSync(Runnable runnable, long period) {
        return this.runRepeatingAsync(runnable, period);
    }


    private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
        return future ->
        {
            if (!future.isCancelled() && future.cause() != null) {
                future.cause().printStackTrace();
            }
        };
    }

    @Override
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[1337]; // What the fuck
    }

    private ViaCommandSender[] getServerPlayers() {
        return new ViaCommandSender[1337]; // What the fuck 2: Electric Boogaloo
    }

    @Override
    public void sendMessage(final UUID uuid, final String s) {
        // Don't even know why this needs to be overridden
    }

    @Override
    public boolean kickPlayer(final UUID uuid, final String s) {
        return false;
    }

    @Override
    public boolean disconnect(UserConnection connection, String message) {
        return ViaPlatform.super.disconnect(connection, message);
    }

    @Override
    public boolean isPluginEnabled() {
        return true;
    }

    @Override
    public ViaAPI<UUID> getApi() {
        return api;
    }

    @Override
    public ViaVersionConfig getConf() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public void onReload() {
        logger.info("ViaVersion was reloaded? (How did that happen)");
    }

    public MCPViaConfig getConfig() {
        return config;
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

    @Override
    public boolean isOldClientsAllowed() {
        return true;
    }

    @Override
    public Collection<UnsupportedSoftware> getUnsupportedSoftwareClasses() {
        return ViaPlatform.super.getUnsupportedSoftwareClasses();
    }

    @Override
    public boolean hasPlugin(final String s) {
        return false;
    }
}
