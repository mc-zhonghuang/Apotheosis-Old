package com.alan.clients.protocol.hyt.germmod.packet.packets;

import com.alan.clients.protocol.hyt.germmod.PacketManager;
import com.alan.clients.protocol.hyt.germmod.gui.GuiButton;
import com.alan.clients.protocol.hyt.germmod.gui.GuiGermMod;
import com.alan.clients.protocol.hyt.germmod.packet.Packet;
import com.alan.clients.util.interfaces.InstanceAccess;
import net.minecraft.network.PacketBuffer;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

public class PacketGui implements Packet, InstanceAccess {
    @Override
    public void write(PacketBuffer buffer) {
    }

    @Override
    public void encode() {
    }

    @Override
    public int packetId() {
        return 0;
    }

    @Override
    public void read(PacketBuffer buffer) {
        int packetId = buffer.readInt();
        if (packetId == 73) {
            PacketBuffer buffer2 = new PacketBuffer(buffer.copy());
            String identity = buffer2.readStringFromBuffer(Short.MAX_VALUE);
            if (identity.equalsIgnoreCase("gui")) {
                String guiId = buffer2.readStringFromBuffer(Short.MAX_VALUE);
                String button = buffer2.readStringFromBuffer(9999999);
                Yaml yaml = new Yaml();
                Map<String, Object> objectMap = yaml.load(button);
                if (objectMap == null) return;
                objectMap = (Map<String, Object>) objectMap.get(guiId);
                if (objectMap == null) return;
                ArrayList<GuiButton> buttons = new ArrayList<>();
                for (String key : objectMap.keySet()) {
                    if (key.equalsIgnoreCase("options") || key.endsWith("_bg")) continue;
                    Map<String, Object> context = (Map<String, Object>) objectMap.get(key);
                    for (String k : context.keySet()) {
                        if (!k.equalsIgnoreCase("scrollableParts")) continue;
                        context = (Map<String, Object>) context.get("scrollableParts");
                        for (String uuid : context.keySet()) {
                            Map<String, Object> scrollableSubMap = (Map<String, Object>) context.get(uuid);
                            if (scrollableSubMap.containsKey("relativeParts")) {
                                scrollableSubMap = (Map<String, Object>) scrollableSubMap.get("relativeParts");
                                for (String k1 : scrollableSubMap.keySet()) {
                                    scrollableSubMap = (Map<String, Object>) scrollableSubMap.get(k1);
                                    if (scrollableSubMap.containsKey("texts")) {
                                        String buttonText = ((ArrayList<String>) scrollableSubMap.get("texts")).get(0);
                                        buttons.add(new GuiButton(buttonText, guiId, key + "$" + uuid + "$" + k1));
                                        break;
                                    }
                                }
                            }
                        }
                        PacketManager.sendPacket(new PacketOpenGui(guiId));
                        mc.displayGuiScreen(new GuiGermMod(guiId, buttons));
                        return;
                    }
                }
            }
        }
    }
}
