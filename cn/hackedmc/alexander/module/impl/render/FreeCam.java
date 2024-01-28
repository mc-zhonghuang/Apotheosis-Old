package cn.hackedmc.alexander.module.impl.render;


import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.CancellableEvent;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.input.MoveInputEvent;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.newevent.impl.motion.StrafeEvent;
import cn.hackedmc.alexander.newevent.impl.other.BlockAABBEvent;
import cn.hackedmc.alexander.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.util.vector.Vector3d;
import cn.hackedmc.alexander.value.impl.NumberValue;
import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

@Getter
@ModuleInfo(name = "module.render.freecam.name", description = "module.render.freecam.description", category = Category.RENDER)
public final class FreeCam extends Module {

    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    private Vector3d position, delta;
    private Vector2f rotation;
    private boolean sprinting;

    @Override
    protected void onEnable() {
        position = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        delta = new Vector3d(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        rotation = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        sprinting = mc.gameSettings.keyBindSprint.isKeyDown();
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.setPosition(position.getX(), position.getY(), position.getZ());
        mc.thePlayer.rotationYaw = rotation.getX();
        mc.thePlayer.rotationPitch = rotation.getY();
        mc.thePlayer.motionX = delta.getX();
        mc.thePlayer.motionY = delta.getY();
        mc.thePlayer.motionZ = delta.getZ();
        mc.gameSettings.keyBindSprint.setPressed(sprinting);
    }

    @EventLink
    public final Listener<BlockAABBEvent> blockAABBEventListener = CancellableEvent::setCancelled;

    @EventLink
    public final Listener<PacketSendEvent> send = event -> {
        Packet<?> packet = event.getPacket();

        if (packet instanceof C0APacketAnimation || packet instanceof C03PacketPlayer ||
                packet instanceof C02PacketUseEntity || packet instanceof C0BPacketEntityAction ||
                packet instanceof C08PacketPlayerBlockPlacement) {
            event.setCancelled();
        }
    };

    @EventLink()
    public final Listener<StrafeEvent> onStrafe = event -> {

        final float speed = this.speed.getValue().floatValue();

        event.setSpeed(speed);
    };

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        final float speed = this.speed.getValue().floatValue();

        mc.thePlayer.motionY = 0.0D
                + (mc.gameSettings.keyBindJump.isKeyDown() ? speed : 0.0D)
                - (mc.gameSettings.keyBindSneak.isKeyDown() ? speed : 0.0D);
    };


    @EventLink()
    public final Listener<MoveInputEvent> onMovementInput = event -> {
        event.setSneak(false);
    };
}