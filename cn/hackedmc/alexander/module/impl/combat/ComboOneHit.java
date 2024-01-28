package cn.hackedmc.alexander.module.impl.combat;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.other.AttackEvent;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.value.impl.NumberValue;
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
