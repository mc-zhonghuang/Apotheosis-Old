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
    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
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
    };
}
