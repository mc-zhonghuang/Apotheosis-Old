package cn.hackedmc.apotheosis.module.impl.combat.criticals;

import cn.hackedmc.apotheosis.module.impl.combat.Criticals;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.AttackEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;
import util.time.StopWatch;

public final class PacketCriticals extends Mode<Criticals> {
    private final NumberValue delay = new NumberValue("Delay", this, 500, 0, 1000, 1);

    private final double[] offsets = new double[]{0.0625, 0};
    private final StopWatch stopwatch = new StopWatch();

    public PacketCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<AttackEvent> onAttack = event -> {
        if(stopwatch.finished(delay.getValue().longValue()) && mc.thePlayer.onGroundTicks > 2) {
            for (final double offset : offsets) {
                PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            }

            mc.thePlayer.onCriticalHit(event.getTarget());
            stopwatch.reset();
        }
    };
}
