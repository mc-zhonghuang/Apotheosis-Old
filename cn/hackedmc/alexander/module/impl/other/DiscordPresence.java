package cn.hackedmc.alexander.module.impl.other;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

@ModuleInfo(name = "module.other.discordrpc.name", description = "module.other.discordrpc.description", category = Category.OTHER, autoEnabled = true)
public class DiscordPresence extends Module {
    private boolean started;

    @EventLink
    private final Listener<TickEvent> onTick = event -> {
        if (!started) {
            final DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder("") {{
                setDetails("Alexander " + Client.VERSION_FULL);
                setBigImage("alexander", "");
                setStartTimestamps(System.currentTimeMillis());
            }};

            DiscordRPC.discordUpdatePresence(builder.build());

            final DiscordEventHandlers handlers = new DiscordEventHandlers();
            DiscordRPC.discordInitialize("1201253156550623302", handlers, true);

            new Thread(() -> {
                while (this.isEnabled()) {
                    DiscordRPC.discordRunCallbacks();
                }
            }, "Discord RPC Callback").start();

            started = true;
        }
    };

    @Override
    protected void onDisable() {
        DiscordRPC.discordShutdown();
        started = false;
    }
}
