package cn.hackedmc.apotheosis.module.impl.player;

import cn.hackedmc.apotheosis.component.impl.player.RotationComponent;
import cn.hackedmc.apotheosis.component.impl.player.rotationcomponent.MovementFix;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.TimeUtil;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import cn.hackedmc.apotheosis.util.packet.PacketUtil;
import cn.hackedmc.apotheosis.util.rotation.RotationUtil;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

@ModuleInfo(name = "module.player.antifireball.name", description = "module.player.antifireball.description", category = Category.PLAYER)
public class AntiFireBall extends Module {
    private final BooleanValue postValue = new BooleanValue("Post", this, false);
    private final BooleanValue sendPostC0FFix = new BooleanValue("SendPostC0FFix",this,  true);
    private final BooleanValue rotation = new BooleanValue("Rotation",this,  true);
    private final NumberValue minRotationSpeed = new NumberValue("MinRotationSpeed", this, 10.0, 0.0, 10, 1);
    private final NumberValue maxRotationSpeed = new NumberValue("MaxRotationSpeed", this, 10.0, 0.0, 10, 1);

    private final BooleanValue moveFix = new BooleanValue("MoveFix",this,  false);
    private final BooleanValue noSwing = new BooleanValue("NoSwing", this, false);
    private final TimeUtil timerUtil = new TimeUtil();


    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (this.postValue.getValue() && event.isPost() || !this.postValue.getValue() && event.isPre()) {
            for(Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityFireball && mc.thePlayer.getDistanceToEntity(entity) < 5.5F && this.timerUtil.delay(300.0F)) {
                    this.timerUtil.reset();
                    if (this.rotation.getValue()) {
                        RotationComponent.setRotations(RotationUtil.getRotationsNonLivingEntity(entity), (double) MathUtil.getRandom2(this.minRotationSpeed.getValue().intValue(), this.maxRotationSpeed.getValue().intValue()), this.moveFix.getValue() ? MovementFix.NORMAL : MovementFix.OFF);
                    }

                    if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                        PacketUtil.sendPacketC0F();
                    }

                    PacketUtil.send(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                    if (this.noSwing.getValue()) {
                        if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                            PacketUtil.sendPacketC0F();
                        }

                        PacketUtil.send(new C0APacketAnimation());
                    } else {
                        if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                            PacketUtil.sendPacketC0F();
                        }

                        mc.thePlayer.swingItem();
                    }
                }
            }
        }
    };
}
