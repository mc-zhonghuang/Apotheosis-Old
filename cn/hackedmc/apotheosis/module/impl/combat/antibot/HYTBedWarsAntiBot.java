package cn.hackedmc.apotheosis.module.impl.combat.antibot;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.combat.AntiBot;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

public final class HYTBedWarsAntiBot extends Mode<AntiBot> {
    public HYTBedWarsAntiBot(String name, AntiBot parent){
        super(name,parent);
    }
    private final ModeValue bedWarsMode = new ModeValue("BedWarsMode",this)
            .add(new SubMode("4v4/1v1"))
            .add(new SubMode("32/64"))
            .add(new SubMode("16v16"))
            .setDefault("4v4/1v1");
    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        Packet<?> packet = event.getPacket();
        if (bedWarsMode.getValue().getName().equals("4v4/1v1") || bedWarsMode.getValue().getName().equals("32/64") || bedWarsMode.getValue().getName().equals("16v16")) {
            if(packet instanceof S14PacketEntity){
                S14PacketEntity packetEntity = (S14PacketEntity) packet;
                Entity entity = packetEntity.getEntity(mc.theWorld);
                if (entity instanceof EntityPlayer && entity.isDead) {
                    if (packetEntity.onGround && !Client.INSTANCE.getBotManager().contains(entity)){
                        Client.INSTANCE.getBotManager().add(entity);
                    }
                }
            }
        }
    };
}
