package cn.hackedmc.apotheosis.module.impl.combat.antibot;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.combat.AntiBot;
import cn.hackedmc.apotheosis.module.impl.combat.KillAura;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;


public final class HytGroundAntiBot extends Mode<AntiBot> {
    public HytGroundAntiBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @EventLink
    private final Listener<PreUpdateEvent> onPreMotionEvent = event -> {
        EntityLivingBase player = (EntityLivingBase) Client.INSTANCE.getModuleManager().get(KillAura.class).target;
        if(player == null) return;
        if (player.height < 1) {
            Client.INSTANCE.getBotManager().add(player);
        } else {
            Client.INSTANCE.getBotManager().remove(player);
        }
    };

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        Packet<?> packet = event.getPacket();
        if(packet instanceof S14PacketEntity){
            S14PacketEntity packetEntity = (S14PacketEntity) packet;
            Entity entity = packetEntity.getEntity(mc.theWorld);
            if (entity instanceof EntityPlayer && entity.isDead) {
                if (packetEntity.onGround && !Client.INSTANCE.getBotManager().contains(entity)){
                    Client.INSTANCE.getBotManager().add(entity);
                }
            }
        }
    };
}

