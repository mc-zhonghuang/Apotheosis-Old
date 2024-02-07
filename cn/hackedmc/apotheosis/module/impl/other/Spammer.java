package cn.hackedmc.apotheosis.module.impl.other;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.util.chat.ChatUtil;
import cn.hackedmc.apotheosis.util.player.ServerUtil;
import cn.hackedmc.apotheosis.value.impl.NumberValue;
import cn.hackedmc.apotheosis.value.impl.StringValue;
import util.time.StopWatch;

@Rise
@ModuleInfo(name = "module.other.spammer.name", description = "module.other.spammer.description", category = Category.OTHER)
public final class  Spammer extends Module {

    private final StringValue message = new StringValue("Message", this, "java.lang.NullPointerException!");
    private final NumberValue delay = new NumberValue("Delay", this, 3000, 0, 20000, 1);

    private final StopWatch stopWatch = new StopWatch();

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (ServerUtil.isOnServer("loyisa.cn")) {
            ChatUtil.display("Upon a request from Loyisa we have blacklisted Loyisa's Test Server from Spammer.");
            this.toggle();
            return;
        }

        if (this.stopWatch.finished(delay.getValue().longValue())) {
            mc.thePlayer.sendChatMessage(message.getValue());
            this.stopWatch.reset();
        }
    };
}
