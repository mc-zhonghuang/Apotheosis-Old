package cn.hackedmc.apotheosis.module.impl.player.scaffold.sprint;

import cn.hackedmc.apotheosis.module.impl.player.Scaffold;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class BypassSprint extends Mode<Scaffold> {

    public BypassSprint(String name, Scaffold parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (MoveUtil.isMoving() && mc.thePlayer.isSprinting() && mc.thePlayer.onGround) {
            final double speed = MoveUtil.WALK_SPEED;
            final float yaw = (float) MoveUtil.direction();
            final double posX = MathHelper.sin(yaw) * speed + mc.thePlayer.posX;
            final double posZ = -MathHelper.cos(yaw) * speed + mc.thePlayer.posZ;
            PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(posX, event.getPosY(), posZ, false));
        }
//        mc.thePlayer.setSprinting(false);
    };
}
