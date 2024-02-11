package cn.hackedmc.apotheosis.module.impl.movement.noweb;

import cn.hackedmc.apotheosis.module.impl.movement.NoWeb;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.ModeValue;
import cn.hackedmc.apotheosis.value.impl.SubMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class GrimACNoWeb extends Mode<NoWeb> {
    public GrimACNoWeb(String name, NoWeb parent) {
        super(name, parent);
    }

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Normal"))
            .add(new SubMode("Expand"))
            .setDefault("Normal");

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        switch (mode.getValue().getName().toLowerCase()) {
            case "normal": {
                if (mc.thePlayer.isInWeb) {
                    for (int i = -1;i <= 1;i++) {
                        for (int i2 = -2;i2 <= 2;i2++) {
                            for (int i3 = -1;i3 <= 1;i3++) {
                                final BlockPos bp = new BlockPos(mc.thePlayer).add(i, i2, i3);
                                final Block block = mc.theWorld.getBlockState(bp).getBlock();
                                if (block instanceof BlockWeb)
                                    mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bp, EnumFacing.UP));
                            }
                        }
                    }

                    mc.thePlayer.isInWeb = false;
                }

                break;
            }

            case "expand": {
                for (int i = -3;i < 3;i++) {
                    for (int i2 = -6;i2 < 6;i2++) {
                        final BlockPos playerPos = new BlockPos(mc.thePlayer);
                        BlockPos[] blockPoses = new BlockPos[]{playerPos.add(i2, i, 7), playerPos.add(i2, i, -7), playerPos.add(7, i, i2), playerPos.add(-7, i, i2)};
                        for (BlockPos blockPos : blockPoses) {
                            final IBlockState blockState = mc.theWorld.getBlockState(blockPos);
                            final Block block = blockState.getBlock();

                            if (block instanceof BlockWeb) {
                                mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                                mc.theWorld.setBlockToAir(blockPos);
                            }
                        }
                    }
                }

                break;
            }
        }
    };
}
