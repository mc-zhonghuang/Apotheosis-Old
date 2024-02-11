package cn.hackedmc.apotheosis.util.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static cn.hackedmc.apotheosis.util.interfaces.InstanceAccess.mc;

public class BlockUtils {

    public static Map<BlockPos, BlockWeb> searchWebBlocks(World world, int radius) {
        Map<BlockPos, BlockWeb> foundBlocks = new HashMap<>();

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        int startX = playerPos.getX() - radius;
        int startY = playerPos.getY() - radius;
        int startZ = playerPos.getZ() - radius;

        int endX = playerPos.getX() + radius;
        int endY = playerPos.getY() + radius;
        int endZ = playerPos.getZ() + radius;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = world.getBlockState(pos).getBlock();
                    if (block instanceof BlockWeb) {
                        foundBlocks.put(pos, (BlockWeb) block);
                    }
                }
            }
        }

        return foundBlocks;
    }
}
