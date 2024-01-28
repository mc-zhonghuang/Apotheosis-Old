package cn.hackedmc.alexander.component.impl.event;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.Priorities;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.AttackEvent;
import cn.hackedmc.alexander.newevent.impl.other.KillEvent;
import cn.hackedmc.alexander.newevent.impl.other.WorldChangeEvent;
import net.minecraft.entity.Entity;

@Rise
//@Priority(priority = -100) /* Must be run before all modules */
public class EntityKillEventComponent extends Component {

    Entity target = null;

    @EventLink(value = Priorities.LOW)
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        threadPool.execute(() -> {
            if (target != null && !mc.theWorld.loadedEntityList.contains(target)) {
                target = null;
                Client.INSTANCE.getEventBus().handle(new KillEvent(target));
            }
        });
    };

    @EventLink(value = Priorities.LOW)
    public final Listener<AttackEvent> onAttackEvent = event -> {
        target = event.getTarget();
    };

    @EventLink(value = Priorities.LOW)
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        target = null;
    };
}
