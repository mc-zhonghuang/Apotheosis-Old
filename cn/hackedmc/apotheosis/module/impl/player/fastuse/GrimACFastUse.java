package cn.hackedmc.apotheosis.module.impl.player.fastuse;

import cn.hackedmc.apotheosis.module.impl.player.FastUse;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class GrimACFastUse extends Mode<FastUse> {
    public GrimACFastUse(String name, FastUse parent) {
        super(name, parent);
    }
    private boolean enable;
    private float lastTimer;

    @Override
    public void onEnable() {
        enable = false;
        lastTimer = 1;
    }

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                if (mc.thePlayer.getItemInUseCount() > 71980) {
                    if (!enable) {
                        enable = true;
                        lastTimer = mc.timer.timerSpeed;
                    }

                    mc.timer.timerSpeed = 0.333F;

                    for (int i = 0;i < 2;i++) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                        mc.thePlayer.itemInUseCount--;
                    }
                } else if (mc.thePlayer.getItemInUseCount() <= 71980) {
                    if (enable) {
                        enable = false;
                        mc.timer.timerSpeed = lastTimer;
                    }

                    if (getParent().autoShot.getValue()) {
                        mc.thePlayer.stopUsingItem();
                        if (!mc.isSingleplayer()) mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                }
            } else if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                if (!enable) {
                    enable = true;
                    lastTimer = mc.timer.timerSpeed;
                }

                mc.timer.timerSpeed = 0.333F;

                for (int i = 0;i < 2 && mc.thePlayer.getItemInUseCount() > 0;i++) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    mc.thePlayer.itemInUseCount--;
                }
            }
        } else {
            if (enable) {
                enable = false;
                mc.timer.timerSpeed = lastTimer;
            }
        }
    };
}
