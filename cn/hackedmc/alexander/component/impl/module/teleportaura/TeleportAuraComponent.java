package cn.hackedmc.alexander.component.impl.module.teleportaura;

import cn.hackedmc.alexander.component.impl.player.RotationComponent;
import cn.hackedmc.alexander.util.pathfinding.unlegit.MainPathFinder;
import cn.hackedmc.alexander.util.pathfinding.unlegit.Vec3;
import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.component.Component;
import cn.hackedmc.alexander.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.alexander.module.impl.combat.NewTeleportAura;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.other.TeleportEvent;
import cn.hackedmc.alexander.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.alexander.newevent.impl.render.Render3DEvent;
import cn.hackedmc.alexander.util.chat.ChatUtil;
import cn.hackedmc.alexander.util.math.MathUtil;
import cn.hackedmc.alexander.util.packet.PacketUtil;
import cn.hackedmc.alexander.util.render.RenderUtil;
import cn.hackedmc.alexander.util.rotation.RotationUtil;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.util.vector.Vector3d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK;

@Rise
public class TeleportAuraComponent extends Component {

    private NewTeleportAura tpa;
    public static boolean enabled;

    public TeleportAuraComponent() {
        this.tpa = getModule(NewTeleportAura.class);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (!enabled) return;

        /*
         * Getting targets and selecting the correct
         */
        tpa.target = getTarget();
        tpa.targetPosition = tpa.target == null ? new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) :
                new Vec3(tpa.target.posX, tpa.target.posY, tpa.target.posZ);
        tpa.targetPosition.setX(Math.floor(tpa.targetPosition.getX()) + 0.5 - Math.random() / 100f);
        tpa.targetPosition.setY(Math.floor(tpa.targetPosition.getY()));
        tpa.targetPosition.setZ(Math.floor(tpa.targetPosition.getZ()) + 0.5 - Math.random() / 100f);

        if (mc.thePlayer.isDead) {
            return;
        }

        next(event);
    };

    private void attack() {
        if (tpa.target != null) {
            if (tpa.stopWatch.finished(tpa.nextSwing)) {

                tpa.attackedList.add(tpa.target);

                mc.thePlayer.swingItem();
                PacketUtil.send(new C02PacketUseEntity(tpa.target, ATTACK));

                final long clicks = (long) (Math.round(MathUtil.getRandom(tpa.cps.getValue().intValue(),
                        tpa.cps.getSecondValue().intValue())) * 1.5);
                tpa.nextSwing = 1000 / clicks;
                tpa.stopWatch.reset();
            }
        } else if (!tpa.isEnabled()) enabled = false;

        tpa.attacked = true;
    }

    private void next(PreMotionEvent preMotionEvent) {

        List<Vec3> path = MainPathFinder.computePath(new Vec3(tpa.position.getX(), tpa.position.getY(), tpa.position.getZ()),
                new Vec3(tpa.targetPosition.getX(), tpa.targetPosition.getY() +
                        (!tpa.stopWatch.finished(tpa.nextSwing) && tpa.target != null ? 5 : 0), tpa.targetPosition.getZ()), false);

        if (path == null) {
            ChatUtil.display("Path was null");
            return;
        }

        switch (tpa.type.getValue().getName()) {
            case "Send":

                break;

            case "Edit":
                tpa.position.setX(Math.floor(tpa.position.getX()) + 0.5);
                tpa.position.setY(Math.floor(tpa.position.getY()));
                tpa.position.setZ(Math.floor(tpa.position.getZ()) + 0.5);

                preMotionEvent.setPosX(tpa.position.getX());
                preMotionEvent.setPosY(tpa.position.getY());
                preMotionEvent.setPosZ(tpa.position.getZ());

                if (tpa.target != null) {
                    final Vector2f targetRotations = RotationUtil.calculate(
                            new Vector3d(tpa.position.getX(), tpa.position.getY(), tpa.position.getZ()),
                            new Vector3d(tpa.target.posX, tpa.target.posY, tpa.target.posZ));
                    RotationComponent.setRotations(targetRotations, 10, MovementFix.OFF);
                }

                if (MathUtil.getDistance(tpa.position.getX(), tpa.position.getY(), tpa.position.getZ(),
                        tpa.targetPosition.getX(), tpa.targetPosition.getY(), tpa.targetPosition.getZ()) < 3) {

                    attack();

                    ChatUtil.display("Called Attack Target: " +
                            (tpa.target == null ? "Null" : tpa.target.getCommandSenderName()));

                    return;
                }

                ChatUtil.display("Set Position");

                tpa.position = path.get(1);

                break;
        }
    }

    public Entity getTarget() {
        if (!tpa.isEnabled()) return null;

        if (!mc.theWorld.loadedEntityList.contains(tpa.target == null ? mc.thePlayer : tpa.target) || tpa.attacked) {
            List<Entity> targets = Client.INSTANCE.getTargetManager().getTargets(tpa.range.getValue().doubleValue());

            Entity target = null;

            switch (tpa.mode.getValue().getName()) {
                case "Single":
                    boolean contains = tpa.attackedList.contains(targets.get(0));
                    tpa.attackedList.clear();
                    target = contains ? null : targets.get(0);
                    break;

                case "Switch":
                    targets.removeIf(entityLivingBase -> tpa.attackedList.contains(entityLivingBase));

                    if (targets.isEmpty()) {
                        tpa.attackedList.clear();
                        target = null;
                    } else {
                        target = targets.get(0);
                    }
                    break;
            }

            tpa.attacked = false;

            return target;
        }

        return tpa.target;
    }

    @EventLink()
    public final Listener<Render3DEvent> onRender3D = event -> {
        if (!enabled || tpa == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDepthMask(false);

        RenderUtil.color(Color.RED);
        RenderUtil.drawBoundingBox(mc.thePlayer.getEntityBoundingBox()
                .offset(-mc.thePlayer.posX, -mc.thePlayer.posY, -mc.thePlayer.posZ)
                .offset(tpa.position.getX(), tpa.position.getY(), tpa.position.getZ()));

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    };

    @EventLink()
    public final Listener<WorldChangeEvent> onWorldChange = event -> {
        enabled = false;
    };

    @EventLink()
    public final Listener<TeleportEvent> onTeleport = event -> {
        if (tpa == null || tpa.position == null) return;
        tpa.position = new Vec3(event.getPosX(), event.getPosY(), event.getPosZ());
    };
}