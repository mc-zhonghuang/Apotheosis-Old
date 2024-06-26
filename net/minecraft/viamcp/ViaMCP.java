package net.minecraft.viamcp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.ProtocolManagerImpl;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.viamcp.gui.AsyncVersionSlider;
import net.minecraft.viamcp.loader.MCPBackwardsLoader;
import net.minecraft.viamcp.loader.MCPRewindLoader;
import net.minecraft.viamcp.loader.MCPViaLoader;
import net.minecraft.viamcp.platform.MCPViaInjector;
import net.minecraft.viamcp.platform.MCPViaPlatform;
import net.minecraft.viamcp.protocols.ProtocolCollection;
import net.minecraft.viamcp.utils.JLoggerToLog4j;
import net.minecraft.viamcp.utils.Platform;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ViaMCP {
    public final static int PROTOCOL_VERSION = 47;
    private static final ViaMCP instance = new ViaMCP();

    public static ViaMCP getInstance() {
        return instance;
    }

    private final Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("ViaMCP"));
    private final CompletableFuture<Void> INIT_FUTURE = new CompletableFuture<>();

    private ExecutorService ASYNC_EXEC;
    private EventLoop EVENT_LOOP;
    private List<Platform> platforms;

    private File file;
    private int version;
    private String lastServer;

    @Getter@Setter
    public static boolean hidden;

    public static void staticInit() {
        getInstance().start();
    }

    /**
     * Version Slider that works Asynchronously with the Version GUI
     * Please initialize this before usage with initAsyncSlider() or initAsyncSlider(x, y, width (min. 110), height)
     */
    public AsyncVersionSlider asyncSlider;

    public void start() {
        try {
            platforms = Arrays.asList(
                    new Platform("ViaVersion", () -> true, () -> {
                        // Empty
                    }, protocolVersions -> protocolVersions.addAll(Arrays.stream(ProtocolCollection.values()).map(ProtocolCollection::getVersion).collect(Collectors.toList()))),
                    new Platform("ViaBackwards", () -> true, () -> new MCPBackwardsLoader(file)),
                    new Platform("ViaRewind", () -> true, () -> new MCPRewindLoader(file))
            );

            final ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ViaMCP-%d").build();
            ASYNC_EXEC = Executors.newFixedThreadPool(8, factory);

            EVENT_LOOP = new LocalEventLoopGroup(1, factory).next();
            EVENT_LOOP.submit(INIT_FUTURE::join);

            for (Platform platform : platforms) {
                platform.createProtocolPath();
            }

            setVersion(PROTOCOL_VERSION);
            this.file = new File("ViaMCP");
            if (this.file.mkdir()) {
                this.getjLogger().info("Creating ViaMCP Folder");
            }

            Via.init(ViaManagerImpl.builder()
                    .injector(new MCPViaInjector())
                    .loader(new MCPViaLoader())
                    .platform(new MCPViaPlatform(file))
                    .build());

            final ViaManagerImpl wrapped = ((ViaManagerImpl) Via.getManager());
            wrapped.addEnableListener(() -> {
                for (Platform platform : platforms) platform.build(jLogger);
            });
            wrapped.init();
            wrapped.onServerLoaded();

            INIT_FUTURE.complete(null);

            ViaMCP.getInstance().initAsyncSlider();
            wrapped.getProtocolManager().setMaxProtocolPathSize(Integer.MAX_VALUE);
            wrapped.getProtocolManager().setMaxPathDeltaIncrease(-1);
            ((ProtocolManagerImpl) wrapped.getProtocolManager()).refreshVersions();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public void initAsyncSlider() {
        asyncSlider = new AsyncVersionSlider(-1, 5, 5, 110, 20);
    }

    public void initAsyncSlider(final int x, final int y, final int width, final int height) {
        asyncSlider = new AsyncVersionSlider(-1, x, y, Math.max(width, 110), height);
    }

    public Logger getjLogger() {
        return jLogger;
    }

    public CompletableFuture<Void> getInitFuture() {
        return INIT_FUTURE;
    }

    public ExecutorService getAsyncExecutor() {
        return ASYNC_EXEC;
    }

    public EventLoop getEventLoop() {
        return EVENT_LOOP;
    }

    public File getFile() {
        return file;
    }

    public String getLastServer() {
        return lastServer;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;

    }

    public void setFile(final File file) {
        this.file = file;
    }

    public void setLastServer(final String lastServer) {
        this.lastServer = lastServer;
    }
}
