package cn.hackedmc.alexander.module.impl.ghost;

import cn.hackedmc.alexander.api.Rise;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;

/**
 * @author Alan Jr. (Not Billionaire)
 * @since 19/9/2022
 */
@Rise
@ModuleInfo(name = "module.ghost.noclickdelay.name", description = "module.ghost.noclickdelay.description", category = Category.GHOST)
public class NoClickDelay extends Module  {

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        if (InstanceAccess.mc.thePlayer != null && InstanceAccess.mc.theWorld != null) {
            InstanceAccess.mc.leftClickCounter = 0;
        }
    };
}