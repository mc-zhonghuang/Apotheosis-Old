package cn.hackedmc.apotheosis.anticheat.check.impl.movement;

import cn.hackedmc.apotheosis.anticheat.check.Check;
import cn.hackedmc.apotheosis.anticheat.check.api.CheckInfo;
import cn.hackedmc.apotheosis.anticheat.data.PlayerData;
import cn.hackedmc.apotheosis.anticheat.util.PacketUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.MathHelper;

@CheckInfo(name = "NoSlow", type = "A", description = "Detects no slows")
public class NoSlow extends Check {
    public NoSlow(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && ((S14PacketEntity) packet).entityId == data.getPlayer().getEntityId() ||
                (packet instanceof S18PacketEntityTeleport && ((S18PacketEntityTeleport) packet).entityId == data.getPlayer().getEntityId())) {
            if (data.getPlayer().isUsingItem()) {
                final EntityOtherPlayerMP player = data.getPlayer();
                if (Math.abs(MathHelper.sqrt_double(player.posX * player.posX + player.posZ * player.posZ) - MathHelper.sqrt_double(player.prevPosX * player.prevPosX + player.prevPosZ * player.prevPosZ)) > 0.1) {
                    fail();
                }
            }
        }
    }
}
