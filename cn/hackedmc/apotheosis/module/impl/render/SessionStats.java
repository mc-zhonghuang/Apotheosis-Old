package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Hidden;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.module.impl.movement.Flight;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.localization.Localization;
import cn.hackedmc.apotheosis.util.player.MoveUtil;
import cn.hackedmc.apotheosis.util.render.ColorUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.other.ServerJoinEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketReceiveEvent;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.impl.*;
import lombok.AllArgsConstructor;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Hazsi
 * @since 10/13/2022
 */
@Rise
@ModuleInfo(name = "module.render.sessionstats.name", description = "module.render.sessionstats.description", category = Category.RENDER)
public class SessionStats extends Module {

    private final ModeValue glowMode = new ModeValue("Glow Mode", this) {{
        add(new SubMode("Colored"));
        add(new SubMode("Shadow"));
        add(new SubMode("None"));
        setDefault("Colored");
    }};

    private final DragValue position = new DragValue("", this, new Vector2d(200, 200), true);
    private Session session = new Session(0, 0, 0, 0, 0, 0);
    private String time = "0 秒";

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {

        if (MoveUtil.isMoving() && MoveUtil.speed() < 0.5 && !mc.thePlayer.inWater &&
                !Client.INSTANCE.getModuleManager().get(Flight.class).isEnabled()) {

            double deltaX = mc.thePlayer.lastTickPosX - mc.thePlayer.posX;
            double deltaZ = mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ;
            double distance = Math.hypot(deltaX, deltaZ);

            if (distance < 5) {
                this.session.distanceWalked += distance;
            }
        } else if (MoveUtil.isMoving() && Client.INSTANCE.getModuleManager().get(Flight.class).isEnabled()) {
            double deltaX = mc.thePlayer.lastTickPosX - mc.thePlayer.posX;
            double deltaZ = mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ;
            double distance = Math.hypot(deltaX, deltaZ);

            this.session.distanceFlown += distance;
        }

        // Don't do this awful shit every frame
        if (mc.thePlayer.ticksExisted % 10 == 0) {
            long elapsed = System.currentTimeMillis() - this.session.startTime;
            long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed) % 60;

            String base = "";
            if (hours > 0) base += hours + (hours == 1 ? "小时" : "小时") + ((minutes == 0 ? "" : ", "));
            if (minutes > 0)
                base += minutes + (minutes == 1 ? "分钟" :"分钟") + (seconds == 0 || hours > 0 ? "" : ", ");
            if (seconds > 0 && hours == 0) base += seconds + (seconds == 1 ? "秒" : "秒");
            this.time = base;
        }
    };

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        double padding = 15;
        position.scale = new Vector2d(200, 100);
        // Don't draw if the F3 menu is open
        if (mc.gameSettings.showDebugInfo) return;
        RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, position.scale.x - 48, position.scale.y - 48, 0, new Color(0,0,0,100));
        // white
        //    RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, position.scale.x - 25, position.scale.y - 10, 0, new Color(255,255,255,20));
        // Blur the area behind the background
        NORMAL_BLUR_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, position.scale.x - 48, position.scale.y - 48, 0, new Color(0,0,0,100));

        });

        // Draw the glow
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            // Shadow glow
            if (this.glowMode.getValue().getName().equals("Shadow")) {
                RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, position.scale.x - 48, position.scale.y - 48, 0, getTheme().getDropShadow());
                RenderUtil.roundedRectangle(position.position.x + 10, position.position.y + 10, position.scale.x - 38, position.scale.y - 38, 0, getTheme().getDropShadow());

            }
        });
        // Draw all the text itself
        NORMAL_POST_RENDER_RUNNABLES.add(() -> {
            // Format the walking/flying distance in meters/km
            FontManager.getProductSansRegular(24).drawStringWithShadow("信息框", position.position.x + padding, position.position.y + padding, -1);

            FontManager.getProductSansRegular(18).drawStringWithShadow(time, position.position.x + padding,
                    position.position.y + padding + 15, new Color(255, 255, 255, 200).getRGB());

            FontManager.getProductSansRegular(18).drawStringWithShadow("杀死:" + " " + session.kills, position.position.x + padding,
                    position.position.y + padding + 25, new Color(255, 255, 255, 200).getRGB());
            FontManager.getProductSansRegular(18).drawStringWithShadow("胜利:" + " " + session.wins, position.position.x + padding,
                    position.position.y + padding + 35, new Color(255, 255, 255, 200).getRGB());
        });


//        if (true) return;
        // Add the glow for the "Session Stats" text and the elapsed time
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            FontManager.getProductSansRegular(24).drawStringWithShadow("信息框", position.position.x + padding, position.position.y + padding, 0xFFFFFFFF);
            FontManager.getProductSansRegular(18).drawStringWithShadow(time, position.position.x + padding, position.position.y + padding + 15, 0xFFFFFFFF);
        });
    };

    @EventLink()
    public final Listener<KillEffect> onKill = event -> {
        this.session.kills++;
    };

    @EventLink()
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = event -> {
        if (event.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle s45 = (S45PacketTitle) event.getPacket();
            if (s45.getMessage() == null) return;

            if (StringUtils.stripControlCodes(s45.getMessage().getUnformattedText()).equals("VICTORY!")) {
                this.session.wins++;
            }
        }
    };

    @EventLink()
    public final Listener<ServerJoinEvent> onServerJoin = event -> {

        this.session = new Session(0, 0, 0, 0, 0, 0);
    };

    @AllArgsConstructor
    private static class Session {
        int kills, wins;
        int userBans, globalBans;
        double distanceWalked, distanceFlown;
        final long startTime = System.currentTimeMillis();
    }
}