package cn.hackedmc.alexander.module.impl.player;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.impl.player.RotationComponent;
import cn.hackedmc.alexander.component.impl.player.SlotComponent;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.newevent.impl.other.TeleportEvent;
import cn.hackedmc.alexander.util.RayCastUtil;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.util.player.SlotUtil;
import cn.hackedmc.alexander.util.rotation.RotationUtil;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.util.vector.Vector3d;
import cn.hackedmc.alexander.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@Rise
@ModuleInfo(name = "module.player.breaker.name", description = "module.player.breaker.description", category = Category.PLAYER)
public class Breaker extends Module {

    public final BooleanValue bed = new BooleanValue("Bed", this, true);

    public final BooleanValue instant = new BooleanValue("Instant", this, true);
    public final BooleanValue throughWalls = new BooleanValue("Through Walls", this, true);
    private final BooleanValue emptySurrounding = new BooleanValue("Empty Surrounding", this, false, () -> !throughWalls.getValue());

    public final BooleanValue rotations = new BooleanValue("Rotate", this, true);
    public final BooleanValue whiteListOwnBed = new BooleanValue("Whitelist Own Bed", this, true);
    private final ListValue<MovementFix> movementCorrection = new ListValue<>("Movement Correction", this);
    private Vector3d block, lastBlock, home;
    private double damage;

    public Breaker() {
        for (MovementFix movementFix : MovementFix.values()) {
            movementCorrection.add(movementFix);
        }

        movementCorrection.setDefault(MovementFix.OFF);
    }

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {

        lastBlock = block;
        block = this.block();

        if (block == null) {
            return;
        }

        if (this.rotations.getValue()) {
            this.rotate();
        }

        if (lastBlock == null || !lastBlock.equals(block)) {
            damage = 0;
        }

        this.destroy();
    };

    public void rotate() {
        final Vector2f rotations = RotationUtil.calculate(block);
        RotationComponent.setRotations(rotations, 5, movementCorrection.getValue());
    }

    public Vector3d block() {
        if (home != null && InstanceAccess.mc.thePlayer.getDistanceSq(home.getX(), home.getY(), home.getZ()) < 35 * 35 && whiteListOwnBed.getValue()) {
            return null;
        }

        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -5; z <= 5; z++) {

                    final Block block = PlayerUtil.blockRelativeToPlayer(x, y, z);
                    final Vector3d position = new Vector3d(InstanceAccess.mc.thePlayer.posX + x, InstanceAccess.mc.thePlayer.posY + y, InstanceAccess.mc.thePlayer.posZ + z);

                    if (!(block instanceof BlockBed)) {
                        continue;
                    }

                    /* Grab moving object position */
                    final MovingObjectPosition movingObjectPosition = RayCastUtil.rayCast(RotationUtil.calculate(position), 4.5f);
                    if (movingObjectPosition == null || movingObjectPosition.hitVec.distanceTo(new Vec3(InstanceAccess.mc.thePlayer.posX, InstanceAccess.mc.thePlayer.posY, InstanceAccess.mc.thePlayer.posZ)) > 4.5) {
                        continue;
                    }

                    if (!throughWalls.getValue()) {
                        final BlockPos blockPos = movingObjectPosition.getBlockPos();
                        if (!blockPos.equalsVector(position)) {
                            continue;
                        }
                    } else if (emptySurrounding.getValue()) {
                        Vector3d addVec = position;
                        double hardness = Double.MAX_VALUE;
                        boolean empty = false;

                        for (int addX = -1; addX <= 1; addX++) {
                            for (int addY = 0; addY <= 1; addY++) {
                                for (int addZ = -1; addZ <= 1; addZ++) {
                                    if (empty || (InstanceAccess.mc.thePlayer.getDistanceSq(position.getX() + addX, position.getY() + addY, position.getZ() + addZ) + 4 > 4.5 * 4.5))
                                        continue;

                                    if (Math.abs(addX) + Math.abs(addY) + Math.abs(addZ) != 1) {
                                        continue;
                                    }

                                    Block possibleBlock = PlayerUtil.block(position.getX() + addX, position.getY() + addY, position.getZ() + addZ);

                                    if (possibleBlock instanceof BlockBed) {
                                        continue;
                                    } else if (possibleBlock instanceof BlockAir) {
                                        empty = true;
                                        continue;
                                    }

                                    double possibleHardness = possibleBlock.getBlockHardness();

                                    if (possibleHardness < hardness) {
                                        hardness = possibleHardness;
                                        addVec = position.add(addX, addY, addZ);
                                    }
                                }
                            }
                        }

                        if (!empty) {
                            if (addVec.equals(position)) {
                                return null;
                            } else {
                                return addVec;
                            }
                        }
                    }

                    return position;
                }
            }
        }

        return null;
    }

    public void updateDamage(final BlockPos blockPos, final double hardness) {
        damage += hardness;
        InstanceAccess.mc.theWorld.sendBlockBreakProgress(InstanceAccess.mc.thePlayer.getEntityId(), blockPos, (int) (damage * 10 - 1));
    }

    public void destroy() {
        final BlockPos blockPos = new BlockPos(block.getX(), block.getY(), block.getZ());
        final double hardness = SlotUtil.getPlayerRelativeBlockHardness(InstanceAccess.mc.thePlayer, InstanceAccess.mc.theWorld, blockPos, SlotComponent.getItemIndex());

        if (instant.getValue()) {
            PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
            PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
            InstanceAccess.mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);

        } else {
            if (damage <= 0) {
                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                if (hardness >= 1) {
                    InstanceAccess.mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                    damage = 0;
                }

                this.updateDamage(blockPos, hardness);
            } else if (damage > 1) {
                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                InstanceAccess.mc.playerController.onPlayerDestroyBlock(blockPos, EnumFacing.DOWN);
                damage = 0;
                this.updateDamage(blockPos, hardness);
            } else {
                this.updateDamage(blockPos, hardness);
            }

            InstanceAccess.mc.thePlayer.swingItem();
        }
    }

    @EventLink()
    public final Listener<TeleportEvent> onTeleport = event -> {

        final double distance = InstanceAccess.mc.thePlayer.getDistance(event.getPosX(), event.getPosY(), event.getPosZ());

        if (distance > 40) {
            home = new Vector3d(event.getPosX(), event.getPosY(), event.getPosZ());
        }
    };
}