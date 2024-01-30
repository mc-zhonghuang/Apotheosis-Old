package cn.hackedmc.apotheosis.module.impl.player.fastuse;

import cn.hackedmc.apotheosis.module.impl.player.FastUse;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class CustomFastUse extends Mode<FastUse> {
    public CustomFastUse(String name, FastUse parent) {
        super(name, parent);
    }
    private final NumberValue amount = new NumberValue("Amount", this, 10, 1, 31, 1);

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                final int usedTick = Math.min(19, amount.getValue().intValue());

                if (mc.thePlayer.getItemInUseCount() == (71980 + usedTick) && getParent().bow.getValue()) {
                    for (int i = 0;i < usedTick;i++) mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    mc.thePlayer.itemInUseCount = 71980;
                    if (getParent().autoShot.getValue()) {
                        mc.thePlayer.stopUsingItem();
                        if (!mc.isSingleplayer()) mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                }
            } else if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                final int usedTick = amount.getValue().intValue();

                if (mc.thePlayer.getItemInUseCount() == usedTick && getParent().food.getValue()) {
                    for (int i = 0;i < usedTick;i++) mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    mc.thePlayer.itemInUseCount -= usedTick;
                    mc.thePlayer.stopUsingItem();
                }
            }
        }
    };
}
