package cn.hackedmc.alexander.module.impl.other;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.value.impl.ModeValue;
import cn.hackedmc.alexander.value.impl.SubMode;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@Rise
@ModuleInfo(name = "module.other.clientspoofer.name", description = "module.other.clientspoofer.description", category = Category.OTHER)
public final class ClientSpoofer extends Module  {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Forge"))
            .add(new SubMode("Lunar"))
            .add(new SubMode("PvP Lounge"))
            .add(new SubMode("CheatBreaker"))
            .add(new SubMode("Geyser"))
            .add(new SubMode("Germ Mod"))
            .setDefault("Forge");

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {

        final Packet<?> packet = event.getPacket();

        if (packet instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload wrapper = (C17PacketCustomPayload) packet;

            switch (mode.getValue().getName()) {
                case "Forge": {
                    wrapper.setData(createPacketBuffer("fml,forge", true));
                    break;
                }

                case "Lunar": {
                    wrapper.setChannel("REGISTER");
                    wrapper.setData(createPacketBuffer("Lunar-Client", false));
                    break;
                }

                case "LabyMod": {
                    wrapper.setData(createPacketBuffer("LMC", true));
                    break;
                }

                case "PvP Lounge": {
                    wrapper.setData(createPacketBuffer("PLC18", false));
                    break;
                }

                case "CheatBreaker": {
                    wrapper.setData(createPacketBuffer("CB", true));
                    break;
                }

                case "Geyser": {
                    // It's meant to be "eyser" don't change it
                    wrapper.setData(createPacketBuffer("eyser", false));
                    break;
                }

                case "Germ Mod": {
//                    wrapper.setData(createPacketBuffer("fml,forge", true));
//                    final C17PacketCustomPayload wrapperGermMod = new C17PacketCustomPayload();
                    wrapper.setChannel("REGISTER");
                    wrapper.setData(createPacketBuffer("FML|HS FML FML|MP FML FORGE germplugin-netease hyt0 armourers", false));
//                    PacketUtil.send(wrapperGermMod);
                }
            }
        }
    };

    private PacketBuffer createPacketBuffer(final String data, final boolean string) {
        if (string) {
            return new PacketBuffer(Unpooled.buffer()).writeString(data);
        } else {
            return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
        }
    }
}
