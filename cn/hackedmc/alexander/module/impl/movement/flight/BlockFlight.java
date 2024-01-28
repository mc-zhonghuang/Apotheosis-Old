package cn.hackedmc.alexander.module.impl.movement.flight;

import cn.hackedmc.alexander.component.impl.player.RotationComponent;
import cn.hackedmc.alexander.module.impl.movement.Flight;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.util.player.PlayerUtil;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author Alan
 * @since 31.03.2022
 */

public class BlockFlight extends Mode<Flight>  {

    public BlockFlight(String name, Flight parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {

        RotationComponent.setRotations(new Vector2f(mc.thePlayer.rotationYaw, 90), 100, MovementFix.OFF);
        if (PlayerUtil.blockRelativeToPlayer(0, -1, 0) instanceof BlockAir) {
            PacketUtil.send(new C0APacketAnimation());

            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                    mc.thePlayer.getCurrentEquippedItem(),
                    new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ),
                    EnumFacing.UP, mc.objectMouseOver.hitVec);
        }
    };
}