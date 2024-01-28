package cn.hackedmc.alexander.module.impl.combat.criticals;

import cn.hackedmc.alexander.module.impl.combat.Criticals;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.AttackEvent;
import cn.hackedmc.alexander.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NCPCriticals extends Mode<Criticals> {
    public NCPCriticals(String name, Criticals parent) {
        super(name, parent);
    }

    private int attacked = 0;

    private final double[] offsets = new double[]{0.00001100134977413, 0.00000000013487744, 0.00000571003114589, 0.00000001578887744};

    @Override
    public void onEnable() {
        attacked = 0;
    }

    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        attacked = 0;
    };

    @EventLink
    public final Listener<AttackEvent> onAttack = event -> {
        attacked++;
        if (attacked >= 3) {
            if (mc.thePlayer.onGround) for (final double offset : offsets) {
                PacketUtil.send(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            }
            attacked = 0;
        }
    };
}
