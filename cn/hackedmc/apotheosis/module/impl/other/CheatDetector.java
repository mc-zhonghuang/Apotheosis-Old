package cn.hackedmc.apotheosis.module.impl.other;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.client.entity.EntityOtherPlayerMP;

@Rise
@ModuleInfo(name = "module.other.cheatdetector.name", description = "module.other.cheatdetector.description", category = Category.OTHER)
public final class CheatDetector extends Module {

    public ModeValue alertType = new ModeValue("Alert Type", this)
            .add(new SubMode("ClientSide"))
            .add(new SubMode("ServerSide"))
            .setDefault("ClientSide");

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        Client.INSTANCE.getCheatDetector().incrementTick();
    };

    @Override
    protected void onDisable() {
        Client.INSTANCE.getCheatDetector().playerMap.clear();
    }

    @Override
    protected void onEnable() {
        Client.INSTANCE.getCheatDetector().playerMap.clear();
        mc.theWorld.playerEntities.forEach(entityPlayer -> {
            if (entityPlayer != mc.thePlayer) {
                Client.INSTANCE.getCheatDetector().getRegistrationListener().handleSpawn((EntityOtherPlayerMP) entityPlayer);
            }
        });
    }

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        Client.INSTANCE.getCheatDetector().playerMap.values().forEach(playerData -> playerData.handle(event.getPacket()));
    };
}
