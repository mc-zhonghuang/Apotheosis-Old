package cn.hackedmc.apotheosis.component.impl.viamcp;

import cn.hackedmc.apotheosis.component.Component;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.viamcp.ViaMCP;

import java.util.List;

import static net.minecraft.util.EnumFacing.*;

public final class BlockHitboxFixComponent extends Component {
    private static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] {new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    @EventLink()
    public final Listener<BlockAABBEvent> onBlockAABB = event -> {

        if (ViaMCP.getInstance().getVersion() > ProtocolVersion.v1_8.getVersion()) {
            final Block block = event.getBlock();

            if (block instanceof BlockLadder) {
                final BlockPos blockPos = event.getBlockPos();
                final IBlockState iblockstate = InstanceAccess.mc.theWorld.getBlockState(blockPos);

                if (iblockstate.getBlock() == block) {
                    final float f = 0.125F + 0.0625f;

                    switch (iblockstate.getValue(BlockLadder.FACING)) {
                        case NORTH:
                            event.setBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F)
                                    .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                            break;

                        case SOUTH:
                            event.setBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f)
                                    .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                            break;

                        case WEST:
                            event.setBoundingBox(new AxisAlignedBB(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
                                    .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                            break;

                        case EAST:
                        default:
                            event.setBoundingBox(new AxisAlignedBB(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F)
                                    .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                    }
                }
            } else if (block instanceof BlockLilyPad) {
                final BlockPos blockPos = event.getBlockPos();

                event.setBoundingBox(new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.09375D, 0.9375D)
                        .offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            } else if (block instanceof BlockPane) {
                final List<AxisAlignedBB> boundingBoxList = event.getBoxList();
                final BlockPane wrapped = (BlockPane) block;
                final BlockPos blockPos = event.getBlockPos();
                final IBlockState state = mc.theWorld.getBlockState(blockPos);

                if (state.getBlock() == block) {
                    offsetAdd(boundingBoxList, blockPos, AABB_BY_INDEX[0]);
                    if (wrapped.canPaneConnectToBlock(mc.theWorld.getBlockState(blockPos.north()).getBlock()))
                    {
                        offsetAdd(boundingBoxList, blockPos, AABB_BY_INDEX[getBoundingBoxIndex(NORTH)]);
                    }

                    if (wrapped.canPaneConnectToBlock(mc.theWorld.getBlockState(blockPos.south()).getBlock()))
                    {
                        offsetAdd(boundingBoxList, blockPos, AABB_BY_INDEX[getBoundingBoxIndex(SOUTH)]);
                    }

                    if (wrapped.canPaneConnectToBlock(mc.theWorld.getBlockState(blockPos.east()).getBlock()))
                    {
                        offsetAdd(boundingBoxList, blockPos, AABB_BY_INDEX[getBoundingBoxIndex(EAST)]);
                    }

                    if (wrapped.canPaneConnectToBlock(mc.theWorld.getBlockState(blockPos.west()).getBlock()))
                    {
                        offsetAdd(boundingBoxList, blockPos, AABB_BY_INDEX[getBoundingBoxIndex(WEST)]);
                    }

                    event.setBoxList(boundingBoxList);
                }
            }
        }
    };

    private static void offsetAdd(final List<AxisAlignedBB> list, final BlockPos blockPos, AxisAlignedBB axisAlignedBB) {
        list.add(axisAlignedBB.offset(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

    private static int getBoundingBoxIndex(EnumFacing p_185729_0_)
    {
        return 1 << p_185729_0_.getHorizontalIndex();
    }
}
