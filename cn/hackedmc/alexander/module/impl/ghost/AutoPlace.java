package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.impl.other.TickEvent;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

@Rise
@ModuleInfo(name = "module.ghost.autoplace.name", description = "module.ghost.autoplace.description", category = Category.GHOST)
public class AutoPlace extends Module {
    private final BooleanValue noSwing = new BooleanValue("No Swing", this, false);

    @EventLink
    public final Listener<TickEvent> onTick = event -> {
        if (mc.objectMouseOver != null) {
            if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.sideHit != EnumFacing.DOWN && mc.objectMouseOver.sideHit != EnumFacing.UP && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) {
                    if (noSwing.getValue()) {
                        PacketUtil.send(new C0APacketAnimation());
                    } else {
                        mc.thePlayer.swingItem();
                    }
                }
            }
        }
    };
}
