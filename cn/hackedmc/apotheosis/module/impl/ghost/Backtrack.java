package cn.hackedmc.apotheosis.module.impl.ghost;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.AttackEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.WorldChangeEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render3DEvent;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.LinkedBlockingQueue;

@ModuleInfo(name = "module.ghost.backtrack.name", description = "module.ghost.backtrack.description", category = Category.GHOST)
public class Backtrack extends Module {
    private final NumberValue range = new NumberValue("Max Range", this, 4, 3, 6, 0.1);
    private final BooleanValue velocity = new BooleanValue("Velocity", this, false);
    private final BooleanValue transaction = new BooleanValue("Transaction", this, false);
    private final BooleanValue blockUpdate = new BooleanValue("Block Update", this, false);

    private int lastAttack = 0;
    private Entity entity;
    private final Vec3 position = new Vec3(-1, -1, -1);
    private final LinkedBlockingQueue<Packet<?>> delayPacket = new LinkedBlockingQueue<>();
    @Override
    protected void onEnable() {
        release();
    }

    @SuppressWarnings("All")
    private void release() {
        lastAttack = 0;
        entity = null;
        while (!delayPacket.isEmpty()) {
            Packet<INetHandlerPlayClient> packet = (Packet<INetHandlerPlayClient>) delayPacket.poll();

            // 处理包前触发 PacketReceiveEvent
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(packet);
            Client.INSTANCE.getEventBus().handle(packetReceiveEvent);

            if (packetReceiveEvent.isCancelled()) {
                continue;
            }

            // 处理包
            packet.processPacket(mc.getNetHandler());
        }
    }

    @EventLink
    private final Listener<PreUpdateEvent> onPreUpdate = event -> {
        if (entity != null && (entity.isDead || (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0))) {
            entity = null;
        }

        if (lastAttack <= 0) {
            if (entity != null)
                release();
        } else {
            lastAttack--;
        }
    };

    @EventLink
    private final Listener<WorldChangeEvent> onWorldChange = event -> release();

    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        if (entity == null) return;

        final Packet<?> packet = event.getPacket();

        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity wrapped = (S14PacketEntity) packet;

            if (wrapped.entityId == entity.getEntityId()) {
                position.xCoord += wrapped.getPosX() / 32D;
                position.yCoord += wrapped.getPosY() / 32D;
                position.zCoord += wrapped.getPosZ() / 32D;

                if (mc.thePlayer.getDistanceSq(position.xCoord, position.yCoord, position.zCoord) >= range.getValue().doubleValue()) {
                    entity = null;
                    release();
                } else {
                    event.setCancelled();
                }
            } else {
                event.setCancelled();
            }
        }

        if (packet instanceof S18PacketEntityTeleport) {
            final S18PacketEntityTeleport wrapped = (S18PacketEntityTeleport) packet;

            if (wrapped.entityId == entity.getEntityId()) {
                position.xCoord = wrapped.posX / 32D;
                position.yCoord = wrapped.posY / 32D;
                position.zCoord = wrapped.posZ / 32D;

                if (mc.thePlayer.getDistanceSq(position.xCoord, position.yCoord, position.zCoord) >= range.getValue().doubleValue()) {
                    entity = null;
                    release();
                } else {
                    event.setCancelled();
                }
            } else {
                event.setCancelled();
            }
        }

        if ((velocity.getValue() && packet instanceof S12PacketEntityVelocity) || (transaction.getValue() && (packet instanceof S32PacketConfirmTransaction || packet instanceof S00PacketKeepAlive)) || (blockUpdate.getValue() && (packet instanceof S23PacketBlockChange || packet instanceof S22PacketMultiBlockChange))) {
            event.setCancelled();

            try {
                delayPacket.put(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @EventLink
    private final Listener<AttackEvent> onAttack = event -> {
        if (entity == null) {
            entity = event.getTarget();
            position.xCoord = entity.posX;
            position.yCoord = entity.posY;
            position.zCoord = entity.posZ;
        }
        lastAttack = 2;
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (entity == null) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDepthMask(false);

        double expand = 0.14;
        RenderUtil.color(ColorUtil.withAlpha(getTheme().getFirstColor(), 100));

        RenderUtil.drawBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(-mc.thePlayer.posX, -mc.thePlayer.posY, -mc.thePlayer.posZ).
                offset(position.xCoord, position.yCoord, position.zCoord).expand(expand, expand, expand));

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    };
}
