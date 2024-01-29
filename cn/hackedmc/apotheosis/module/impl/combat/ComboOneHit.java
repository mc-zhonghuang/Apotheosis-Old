package cn.hackedmc.apotheosis.module.impl.combat;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.other.AttackEvent;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.network.play.client.C02PacketUseEntity;

@Rise
@ModuleInfo(name = "module.combat.comboonehit.name", description = "module.combat.comboonehit.description", category = Category.COMBAT)
public final class ComboOneHit extends Module {

    public final NumberValue packets = new NumberValue("Attack Packets", this, 50, 1, 1000, 25);

    @EventLink()
    public final Listener<AttackEvent> onAttack = event -> {
        for (int i = 0; i < packets.getValue().intValue(); i++) {
            PacketUtil.send(new C02PacketUseEntity(event.getTarget(), C02PacketUseEntity.Action.ATTACK));
        }
    };
}
