package cn.hackedmc.apotheosis.module.impl.movement.noweb;

import cn.hackedmc.apotheosis.module.impl.movement.Noweb;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.util.player.BlockUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import net.minecraft.block.BlockWeb;
import java.util.Map;
import cn.hackedmc.apotheosis.newevent.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import java.util.concurrent.*;

public class VanillaNoweb extends Mode<Noweb> {
    private boolean hasSentPacket = false;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // 创建一个单线程池用于调度器

    public VanillaNoweb(String name, Noweb parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (hasSentPacket) {
            return; // 如果已发送包，则直接返回，不执行后续操作
        }

        World world = Minecraft.getMinecraft().theWorld;
        Map<BlockPos, BlockWeb> searchBlocks = BlockUtils.searchWebBlocks(world, 4);

        // 是否已经找到了蜘蛛网
        boolean foundWeb = false;

        for (Map.Entry<BlockPos, BlockWeb> entry : searchBlocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockWeb block = entry.getValue();

            if (block != null) {
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                player.isInWeb = false;

                foundWeb = true;
                hasSentPacket = true; // 设置已发送包标志

                break;
            }
        }

        if (foundWeb) {
            scheduler.schedule(() -> {
                hasSentPacket = false;
            }, 10, TimeUnit.MILLISECONDS);
        }
    };
}
//不知道