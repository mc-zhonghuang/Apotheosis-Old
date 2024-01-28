package cn.hackedmc.alexander.module.impl.combat.criticals;

import cn.hackedmc.alexander.module.impl.combat.Criticals;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class GrimACCriticals extends Mode<Criticals> {
    public GrimACCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    private boolean attacking = false;

    @EventLink
    private final Listener<BlockAABBEvent> onBlockAABB = event -> {
        if (attacking) {
            event.setBoundingBox(null);
        }
    };

    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            attacking = false;
        }

        if (packet instanceof C02PacketUseEntity) {
            final C02PacketUseEntity wrapped = (C02PacketUseEntity) packet;

            if (wrapped.getAction() == C02PacketUseEntity.Action.ATTACK) {
                for (int xOffset = -1;xOffset <= 1;xOffset++) {
                    for (int zOffset = -1;zOffset <= 1;zOffset++) {
                        mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer).add(xOffset, -1, zOffset), EnumFacing.UP));
                    }
                }
                attacking = true;
            }
        }
    };
}
