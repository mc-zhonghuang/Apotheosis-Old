package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.util.RayCastUtil;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.impl.other.AttackEvent;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.value.impl.NumberValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MovingObjectPosition;
import util.type.EvictingList;


/**
 * @author Alan
 * @since 29/01/2021
 */

@Rise
@ModuleInfo(name = "module.ghost.aimbacktrack.name", description = "module.ghost.aimbacktract.description", category = Category.GHOST)
public class AimBacktrack extends Module {

    private final NumberValue backtrack = new NumberValue("Rotation Backtrack Amount", this, 1, 1, 20, 1);
    private EvictingList<Vector2f> previousRotations = new EvictingList<>(1);
    private boolean attacked;
    private int lastSize;

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (lastSize != backtrack.getValue().intValue()) {
            previousRotations = new EvictingList<>(backtrack.getValue().intValue());
            lastSize = backtrack.getValue().intValue();
        }

        previousRotations.add(new Vector2f(event.getYaw(), event.getPitch()));

        attacked = false;
    };

    @EventLink()
    public final Listener<PacketSendEvent> onPacketSend = event -> {

        final Packet<?> packet = event.getPacket();

        if (packet instanceof C0APacketAnimation && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
            for (final Vector2f rotation : previousRotations) {
                final Reach reach = this.getModule(Reach.class);
                final MovingObjectPosition movingObjectPosition = RayCastUtil.rayCast(rotation, reach.isEnabled() ? 3.0D + reach.range.getValue().doubleValue() : 3.0D);

                if (movingObjectPosition.entityHit != null && !attacked) {
                    final AttackEvent e = new AttackEvent(movingObjectPosition.entityHit);
                    Client.INSTANCE.getEventBus().handle(e);

                    if (e.isCancelled()) return;
                    mc.playerController.attackEntity(mc.thePlayer, movingObjectPosition.entityHit);
                }
            }
        }
    };

    @EventLink()
    public final Listener<AttackEvent> onAttack = event -> {
        if (attacked) {
            event.setCancelled(true);
        }
        attacked = true;
    };
}