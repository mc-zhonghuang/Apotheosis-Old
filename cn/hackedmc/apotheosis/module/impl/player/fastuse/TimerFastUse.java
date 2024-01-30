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

public class TimerFastUse extends Mode<FastUse> {
    public TimerFastUse(String name, FastUse parent) {
        super(name, parent);
    }

    private final NumberValue timer = new NumberValue("Speed", this, 2, 0.1, 10, 0.1);
    private boolean enabled;
    private float lastTimer;

    @Override
    public void onEnable() {
        enabled = false;
        lastTimer = 1;
    }

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                if (!getParent().bow.getValue()) return;

                if (mc.thePlayer.getItemInUseCount() > 71980) {
                    if (!enabled) {
                        enabled = true;
                        lastTimer = mc.timer.timerSpeed;
                    }

                    mc.timer.timerSpeed = this.timer.getValue().floatValue();
                } else if (mc.thePlayer.getItemInUseCount() == 71980) {
                    if (enabled) {
                        enabled = false;
                        mc.timer.timerSpeed = lastTimer;
                    }

                    if (getParent().autoShot.getValue()) {
                        mc.thePlayer.stopUsingItem();
                        if (!mc.isSingleplayer()) mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                }
            } else {
                if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
                    if (!getParent().food.getValue()) return;

                    if (!enabled) {
                        enabled = true;
                        lastTimer = mc.timer.timerSpeed;
                    }

                    mc.timer.timerSpeed = this.timer.getValue().floatValue();
                }
            }
        } else {
            if (enabled) {
                enabled = false;
                mc.timer.timerSpeed = lastTimer;
            }
        }
    };
}
