package cn.hackedmc.alexander.module.impl.other;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.impl.render.NotificationComponent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;

@Rise
@ModuleInfo(name = "module.other.hypixelautoplay.name", description = "module.other.autogg.description", category = Category.OTHER)
public final class HypixelAutoPlay extends Module {

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        Packet<?> packet = event.getPacket();

        if (packet instanceof S02PacketChat) {
            S02PacketChat chat = ((S02PacketChat) packet);

            if (chat.getChatComponent().getFormattedText().contains("play again?") || chat.getChatComponent().getFormattedText().contains("再来一局")) {
                for (IChatComponent iChatComponent : chat.getChatComponent().getSiblings()) {
                    for (String value : iChatComponent.toString().split("'")) {
                        if (value.startsWith("/play") && !value.contains(".")) {
                            ChatUtil.send(value);
                            NotificationComponent.post("Auto Play", "Joined a new game", 7000);
                            break;
                        }
                    }
                }
            }
        }
    };

}
