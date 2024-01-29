package cn.hackedmc.apotheosis.module.impl.combat.regen;

import cn.hackedmc.apotheosis.component.impl.player.RotationComponent;
import cn.hackedmc.apotheosis.module.impl.combat.Regen;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;

public class HigherVersionRegen extends Mode<Regen> {
    public HigherVersionRegen(String name, Regen parent) {
        super(name, parent);
    }
    private final NumberValue health = new NumberValue("Minimum Health", this, 15, 1, 20, 1);

    @EventLink
    private final Listener<PreMotionEvent> onPreMotion = event -> {
        if (mc.thePlayer.getHealth() < this.health.getValue().floatValue()) {
            for (int i = 0;i < 40;i++) {
                mc.getNetHandler().addToSendQueueUnregistered(new C03PacketPlayer.C06PacketPlayerPosLook(
                        mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        RotationComponent.rotations.x,
                        RotationComponent.rotations.y,
                        mc.thePlayer.onGround
                ));
            }
        }
    };
}
