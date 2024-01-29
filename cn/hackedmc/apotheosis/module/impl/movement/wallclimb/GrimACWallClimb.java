package cn.hackedmc.apotheosis.module.impl.movement.wallclimb;

import cn.hackedmc.apotheosis.module.impl.movement.WallClimb;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.input.MoveInputEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class GrimACWallClimb extends Mode<WallClimb> {
    public GrimACWallClimb(String name, WallClimb parent) {
        super(name, parent);
    }
    private final BooleanValue sneak = new BooleanValue("Legit", this, false);
    private boolean inWall;
    private BlockPos targetPos;

    @Override
    public void onEnable() {
        reset();
    }

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (inWall && targetPos != null && (Math.abs(targetPos.getX() - mc.thePlayer.posX) >= 1.5 || Math.abs(targetPos.getZ() - mc.thePlayer.posZ) >= 1.5))
            reset();

        if (inWall) {
            if (targetPos == null) return;

           targetPos = new BlockPos(targetPos.getX(), Math.floor(mc.thePlayer.posY), targetPos.getZ());

           final BlockPos blockNeedBreak = targetPos.up(2);

           if (cantBreak(mc.theWorld.getBlockState(blockNeedBreak).getBlock())) {
               reset();

               return;
           }

            mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockNeedBreak, EnumFacing.DOWN));
            for (int i = 0;i <= 1;i++) {
                final BlockPos offsetPos = targetPos.up(i);
                if (cantBreak(mc.theWorld.getBlockState(offsetPos).getBlock())) continue;
                mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, offsetPos, EnumFacing.DOWN));
            }
        } else {
            final float yaw = (float) Math.toRadians(mc.thePlayer.rotationYaw);
            targetPos = new BlockPos(mc.thePlayer.posX - MathHelper.sin(yaw), mc.thePlayer.posY, mc.thePlayer.posZ + MathHelper.cos(yaw));
            for (int i = 0;i <= 1;i++) {
                final BlockPos offsetPos = targetPos.add(0, i, 0);
                if (cantBreak(mc.theWorld.getBlockState(offsetPos).getBlock())) continue;
                mc.getNetHandler().addToSendQueueUnregistered(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, offsetPos, EnumFacing.DOWN));
            }
        }
    };

    @EventLink
    private final Listener<BlockAABBEvent> onBlockAABB = event -> {
        final BlockPos blockPos = event.getBlockPos();

        if (targetPos == null || event.getBlock() instanceof BlockLadder) return;

        if (blockPos.equals(targetPos) || blockPos.equals(targetPos.up()) || (blockPos.equals(targetPos.up(2)) && inWall))
            event.setBoundingBox(null);
    };

    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();

        if (targetPos == null || inWall) return;

        if (packet instanceof S08PacketPlayerPosLook) {
            inWall = true;
        }
    };

    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging wrapped = (C07PacketPlayerDigging) packet;

            if (wrapped.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK && targetPos != null)
                event.setCancelled();
        }
    };

    @EventLink
    private final Listener<MoveInputEvent> onMoveInput = event -> {
        if (sneak.getValue() && targetPos != null)
            event.setSneak(true);
    };

    private boolean cantBreak(Block block) {
        return block instanceof BlockAir || block instanceof BlockLadder;
    }

    private void reset() {
        inWall = false;
        targetPos = null;
    }
}
