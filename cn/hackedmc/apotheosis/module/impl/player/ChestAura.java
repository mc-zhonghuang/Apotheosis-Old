package cn.hackedmc.apotheosis.module.impl.player;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.component.impl.player.RotationComponent;
import cn.hackedmc.apotheosis.component.impl.player.SlotComponent;
import cn.hackedmc.apotheosis.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.combat.KillAura;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.util.RandomUtil;
import cn.hackedmc.apotheosis.util.RayCastUtil;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.util.rotation.RotationUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2f;
import cn.hackedmc.apotheosis.util.vector.Vector3d;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.BoundsNumberValue;
import cn.hackedmc.apotheosis.value.impl.ListValue;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import util.time.StopWatch;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "module.player.chestaura.name", description = "module.player.chestaura.description", category = Category.PLAYER)
public class ChestAura extends Module {
    private final NumberValue range = new NumberValue("Range", this, 3, 1, 6, 0.1);
    private final BooleanValue rotation = new BooleanValue("Rotation", this, false);
    private final BoundsNumberValue rotationSpeed = new BoundsNumberValue("Rotation Speed", this, 5, 10, 1, 10, 1, () -> !rotation.getValue());
    private final ListValue<MovementFix> movementCorrection = new ListValue<>("Movement Correction", this, () -> !rotation.getValue());
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 50, 100, 0, 5000, 50);
    private final StopWatch stopWatch = new StopWatch();
    private long nextWait = 0;
    private final List<BlockPos> found = new ArrayList<>();

    public static boolean disabler = false;

    public ChestAura() {
        for (MovementFix movementFix : MovementFix.values()) {
            movementCorrection.add(movementFix);
        }

        movementCorrection.setDefault(MovementFix.OFF);
    }

    @Override
    protected void onEnable() {
        stopWatch.reset();
        found.clear();
    }

    @EventLink
    private final Listener<WorldChangeEvent> onWorld = event -> {
        found.clear();
        stopWatch.reset();
    };

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (disabler){
            this.toggle();
        }
        if (!stopWatch.finished(nextWait) || mc.currentScreen != null || BlinkComponent.blinking) return;
        int reach = range.getValue().intValue();
        if (KillAura.INSTANCE.range.getValue().floatValue() > 3){
            Client.INSTANCE.getModuleManager().get(ChestAura.class).onDisable();
        }
        for (int x = -reach;x <= reach; x++) {
            for (int y = -reach;y <= reach; y++) {
                for (int z = -reach;z <= reach; z++) {
                    final BlockPos blockPos = new BlockPos(mc.thePlayer).add(x, y, z);
                    if (found.contains(blockPos)) continue;

                    final Block block = PlayerUtil.blockRelativeToPlayer(x, y, z);
                    final Vector3d position = new Vector3d(InstanceAccess.mc.thePlayer.posX + x, InstanceAccess.mc.thePlayer.posY + y, InstanceAccess.mc.thePlayer.posZ + z);

                    if (block instanceof BlockChest) {
                        final Vector2f vector2f = RotationUtil.calculate(position);
                        final MovingObjectPosition cast = RayCastUtil.rayCast(vector2f, reach);

                        if (cast.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.theWorld.getBlockState(cast.getBlockPos()).getBlock() instanceof BlockChest && cast.getBlockPos().equals(blockPos)) {
                            if (rotation.getValue()) {
                                RotationComponent.setRotations(vector2f, RandomUtil.nextInt(rotationSpeed.getValue().intValue(), rotationSpeed.getSecondValue().intValue()), movementCorrection.getValue());

                                final MovingObjectPosition result = RayCastUtil.rayCast(RotationComponent.rotations, reach);
                                if (result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.theWorld.getBlockState(result.getBlockPos()).getBlock() instanceof BlockChest && result.getBlockPos().equals(blockPos)) {
                                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, SlotComponent.getItemStack(), result.getBlockPos(), result.sideHit, result.hitVec);
                                    found.add(blockPos);
                                    nextWait = RandomUtil.nextInt(delay.getValue().intValue(), delay.getSecondValue().intValue());
                                    stopWatch.reset();
                                    return;
                                }
                            } else {
                                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, SlotComponent.getItemStack(), cast.getBlockPos(), cast.sideHit, cast.hitVec);
                                found.add(blockPos);
                                nextWait = RandomUtil.nextInt(delay.getValue().intValue(), delay.getSecondValue().intValue());
                                stopWatch.reset();
                                return;
                            }
                        }
                    }
                }
            }
        }
    };
}
