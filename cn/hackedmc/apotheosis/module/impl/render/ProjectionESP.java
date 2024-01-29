package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.component.impl.render.ProjectionComponent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import net.minecraft.entity.player.EntityPlayer;

import javax.vecmath.Vector4d;
import java.awt.*;

/**
 * @author Hazsi, Alan
 * @since 10/11/2022
 */
@Rise
@ModuleInfo(name = "module.render.2desp.name", description = "module.render.projectionesp.description", category = Category.RENDER)
public class ProjectionESP extends Module {

    public BooleanValue glow = new BooleanValue("Glow", this, true);

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {

        for (EntityPlayer player : InstanceAccess.mc.theWorld.playerEntities) {
            if (InstanceAccess.mc.getRenderManager() == null || player == InstanceAccess.mc.thePlayer ||
                    !RenderUtil.isInViewFrustrum(player) || player.isDead || player.isInvisible()) {
                continue;
            }

            Vector4d pos = ProjectionComponent.get(player);

            if (pos == null) {
                return;
            }

            // Black outline
            RenderUtil.rectangle(pos.x, pos.y, pos.z - pos.x, 1.5, Color.BLACK); // Top
            RenderUtil.rectangle(pos.x, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Left
            RenderUtil.rectangle(pos.z, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Right
            RenderUtil.rectangle(pos.x, pos.w, pos.z - pos.x, 1.5, Color.BLACK); // Bottom

            // Main ESP
            Runnable runnable = () -> {

                final Vector2d first = new Vector2d(0, 0), second = new Vector2d(0, 500);

                RenderUtil.horizontalGradient(pos.x + 0.5, pos.y + 0.5, pos.z - pos.x, 0.5, // Top
                        this.getTheme().getAccentColor(first), this.getTheme().getAccentColor(second));
                RenderUtil.verticalGradient(pos.x + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Left
                        this.getTheme().getAccentColor(first), this.getTheme().getAccentColor(second));
                RenderUtil.verticalGradient(pos.z + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Right
                        this.getTheme().getAccentColor(second), this.getTheme().getAccentColor(first));
                RenderUtil.horizontalGradient(pos.x + 0.5, pos.w + 0.5, pos.z - pos.x, 0.5, // Bottom
                        this.getTheme().getAccentColor(second), this.getTheme().getAccentColor(first));
            };

            runnable.run();
            if (this.glow.getValue()) {
                InstanceAccess.NORMAL_POST_BLOOM_RUNNABLES.add(runnable);
            }
        }
    };
}
