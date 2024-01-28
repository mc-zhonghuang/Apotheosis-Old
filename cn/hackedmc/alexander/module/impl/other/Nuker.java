package cn.hackedmc.alexander.module.impl.other;

import cn.hackedmc.alexander.component.impl.player.GUIDetectionComponent;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.util.pathfinding.unlegit.MainPathFinder;
import cn.hackedmc.alexander.util.pathfinding.unlegit.Vec3;
import cn.hackedmc.alexander.util.rotation.RotationUtil;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.util.vector.Vector3d;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.ModeValue;
import cn.hackedmc.alexander.value.impl.NumberValue;
import cn.hackedmc.alexander.value.impl.SubMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import util.time.StopWatch;

import java.util.Collections;
import java.util.List;

@Rise
@ModuleInfo(name = "module.other.nuker.name", description = "module.other.nuker.description", category = Category.OTHER)
public final class Nuker extends Module {

    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new SubMode("Nearby"))
            .add(new SubMode("Teleport"))
            .setDefault("Nearby");

    private final NumberValue delay = new NumberValue("Delay", this, 150, 0, 5000, 50);
    private final NumberValue range = new NumberValue("Range", this, 4, 1, 6, 0.5);
    private final BooleanValue rotations = new BooleanValue("Rotations", this, false);
    private final BooleanValue scatter = new BooleanValue("Scatter", this, false);
    private final BooleanValue swing = new BooleanValue("Swing", this, false);

    private final StopWatch stopWatch = new StopWatch();

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (GUIDetectionComponent.inGUI()) return;

        final double range = this.range.getValue().doubleValue();

        switch (mode.getValue().getName()) {
            case "Nearby":
                if (stopWatch.finished(delay.getValue().longValue())) {
                    new Thread(() -> nuke(range, event.getPosX(), event.getPosY(), event.getPosZ())).start();
                    stopWatch.reset();
                }
                break;

            case "Teleport":
                if (mc.gameSettings.keyBindUseItem.isKeyDown() && stopWatch.finished(delay.getValue().longValue())) {
                    final BlockPos target = mc.thePlayer.rayTrace(999, 1).getBlockPos();

                    if (mc.theWorld.getBlockState(target).getBlock() instanceof BlockAir) {
                        return;
                    }

                    if (mc.thePlayer.capabilities.allowFlying && !mc.thePlayer.capabilities.isFlying) {
                        final PlayerCapabilities capabilities = mc.thePlayer.capabilities;
                        capabilities.isFlying = true;
                        PacketUtil.sendNoEvent(new C13PacketPlayerAbilities(capabilities));
                    }

                    new Thread(() -> {
                        final List<Vec3> path = MainPathFinder.computePath(new Vec3(event.getPosX(), event.getPosY(), event.getPosZ()), new Vec3(target.getX(), target.getY(), target.getZ()), false);

                        if (path == null) {
                            return;
                        }

                        for (final Vec3 vec : path) {
                            PacketUtil.sendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), false));
                        }

                        nuke(range, target.getX(), target.getY(), target.getZ());
                        Collections.reverse(path);

                        for (final Vec3 vec : path) {
                            PacketUtil.sendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), false));
                        }
                    }).start();

                    stopWatch.reset();
                }
                break;
        }


    };


    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {

        final Packet<?> p = event.getPacket();

        if (p instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) p;

            if (!wrapper.isChat() && wrapper.getChatComponent().getUnformattedText().equals("You can't build outside the plot!")) {
                event.setCancelled(true);
            }
        }
    };

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        this.toggle();
    };

    private void nuke(final double range, final double x, final double y, final double z) {
        for (double posX = -range; posX < range; posX++) {
            for (double posY = range; posY > -range; posY--) {
                for (double posZ = -range; posZ < range; posZ++) {
                    if (scatter.getValue() && !((mc.thePlayer.ticksExisted % 2 == 0 ? posX : posZ) % 2 == 0)) {
                        continue;
                    }

                    final BlockPos blockPos = new BlockPos(x + posX, y + posY, z + posZ);
                    final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                    if (block instanceof BlockAir) {
                        continue;
                    }

                    if (rotations.getValue()) {
                        final Vector2f rotations = RotationUtil.calculate(new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                        PacketUtil.sendNoEvent(new C03PacketPlayer.C05PacketPlayerLook(rotations.x, rotations.y, false));
                    }
                    PacketUtil.sendNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));

                    if (swing.getValue()) {
                        mc.thePlayer.swingItem();
                    }
                }
            }
        }

//        final BlockPos blockPos = new BlockPos(x, y, z);
//        final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
//
//        if (block instanceof BlockAir) {
//            return;
//        }
//
//        if (rotations.getValue()) {
//            final Vector2f rotations = RotationUtil.calculate(new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
//            PacketUtil.sendNoEvent(new C03PacketPlayer.C05PacketPlayerLook(rotations.x, rotations.y, false));
//        }
//        PacketUtil.sendNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
//        PacketUtil.sendNoEvent(new C08PacketPlayerBlockPlacement(blockPos, EnumFacing.UP.getIndex(), mc.thePlayer.getHeldItem(), 255, 255, 255));
//        PacketUtil.sendNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
//
//        if (swing.getValue()) {
//            mc.thePlayer.swingItem();
//        }
    }
}