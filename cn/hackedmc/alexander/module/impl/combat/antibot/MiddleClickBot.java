package cn.hackedmc.alexander.module.impl.combat.antibot;

import cn.hackedmc.alexander.Client;
import cn.hackedmc.alexander.bots.BotManager;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.module.impl.combat.AntiBot;
import cn.hackedmc.alexander.value.Mode;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;

public final class MiddleClickBot extends Mode<AntiBot>  {

    private boolean down;

    public MiddleClickBot(String name, AntiBot parent) {
        super(name, parent);
    }

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (Mouse.isButtonDown(2)) {
            if (down) return;
            down = true;

            if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                BotManager botManager = Client.INSTANCE.getBotManager();
                Entity entity = mc.objectMouseOver.entityHit;

                if (botManager.contains(entity)) {
                    Client.INSTANCE.getBotManager().remove(entity);
                } else {
                    Client.INSTANCE.getBotManager().add(entity);
                }
            }
        } else down = false;
    };
}