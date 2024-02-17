package cn.hackedmc.apotheosis.module.impl.player.antivoid;

import cn.hackedmc.apotheosis.component.impl.player.BlinkComponent;
import cn.hackedmc.apotheosis.component.impl.player.FallDistanceComponent;
import cn.hackedmc.apotheosis.module.impl.movement.LongJump;
import cn.hackedmc.apotheosis.module.impl.player.AntiVoid;
import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.util.player.PlayerUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.util.Vec3;

public class BlinkAntiVoid extends Mode<AntiVoid> {

    private final NumberValue distance = new NumberValue("Distance", this, 5, 0, 10, 1);
    private final BooleanValue toggleScaffold = new BooleanValue("Toggle Scaffold", this, false);
    private Vec3 position, motion;
    private boolean wasVoid, setBack;
    private int overVoidTicks;
    private Scaffold scaffold;
    private LongJump longJump;

    public BlinkAntiVoid(String name, AntiVoid parent) {
        super(name, parent);
    }

    @Override
    public void onDisable() {
        BlinkComponent.blinking = false;
    }

    @EventLink()
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {

        if (mc.thePlayer.ticksExisted <= 50) return;

        if (scaffold == null) {
            scaffold = getModule(Scaffold.class);
        }

        if (longJump == null) {
            longJump = getModule(LongJump.class);
        }

        if (scaffold.isEnabled() || longJump.isEnabled()) {
            return;
        }

        boolean overVoid = !mc.thePlayer.onGround && !PlayerUtil.isBlockUnder(30, true);

        if (overVoid) {
            overVoidTicks++;
        } else if (mc.thePlayer.onGround) {
            overVoidTicks = 0;
        }

        if (overVoid && position != null && motion != null && overVoidTicks < 30 + distance.getValue().doubleValue() * 20) {
            if (!setBack) {
                wasVoid = true;

                BlinkComponent.blinking = true;
                BlinkComponent.setExempt(C0FPacketConfirmTransaction.class, C00PacketKeepAlive.class, C01PacketChatMessage.class);

                if (FallDistanceComponent.distance > distance.getValue().doubleValue() || setBack) {
                    PacketUtil.sendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(position.xCoord, position.yCoord - 0.1 - Math.random(), position.zCoord, false));
                    if (this.toggleScaffold.getValue()) {
                        getModule(Scaffold.class).setEnabled(true);
                    }

                    BlinkComponent.packets.clear();

                    FallDistanceComponent.distance = 0;

                    setBack = true;
                }
            } else {
                BlinkComponent.blinking = false;
            }
        } else {

            setBack = false;

            if (wasVoid) {
                BlinkComponent.blinking = false;
                wasVoid = false;
            }

            motion = new Vec3(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
            position = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        }
    };
}