package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.component.impl.render.ProjectionComponent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.fucker.Fucker;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.font.impl.minecraft.FontRenderer;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import javax.vecmath.Vector4d;
import java.awt.*;

/**
 * @author Alan
 * @since 29/04/2022
 */
@Rise
@ModuleInfo(name = "module.render.nametags.name", description = "module.render.nametags.description", category = Category.RENDER)
public final class NameTags extends Module {
    private final BooleanValue health = new BooleanValue("Show Health", this, true);
    // Show health option doesn't work until we come up with a design that looks good without the health
    // To be honest I don't care alan

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {

        Font nunitoLight14 = FontManager.getNunitoLight(14);
        for (Entity entity : Client.INSTANCE.getTargetManager()) {
            if (entity == mc.thePlayer || !(entity instanceof EntityLivingBase)) {
                continue;
            }

            entity.renderNameTag = false;

            Vector4d position = ProjectionComponent.get(entity);

            if (position == null) {
                continue;
            }

            String playerName = entity.getCommandSenderName();
            String ircName = Fucker.usernames.get(playerName);
            String text;
            if (ircName != null) {
                text = playerName + " §r(§b" + ircName + "§r)";
            } else {
                text = playerName;
            }
            final double nameWidth = nunitoLightNormal.width(text);

            final double posX = (position.x + (position.z - position.x) / 2);
            final double posY = position.y - 2;
            final double margin = 2;

            final int multiplier = 2;
            final double nH = nunitoLightNormal.height() + (this.health.getValue() ? nunitoLight14.height() : 0) + margin * multiplier;
            final double nY = posY - nH;

            NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(posX - margin - nameWidth / 2, nY, nameWidth + margin * multiplier, nH, getTheme().getRound(), getTheme().getDropShadow());
            });

            NORMAL_RENDER_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(posX - margin - nameWidth / 2, nY, nameWidth + margin * multiplier, nH, getTheme().getRound(), getTheme().getBackgroundShade());
                nunitoLightNormal.drawCenteredString(text, posX, nY + margin * 2, getTheme().getFirstColor().getRGB());

                if (this.health.getValue()) {
                    nunitoLight14.drawCenteredString(String.valueOf((int) ((EntityLivingBase) entity).getHealth()), posX, posY + 1 + 3 - margin - FontRenderer.FONT_HEIGHT, Color.WHITE.hashCode());
                }
            });

            NORMAL_BLUR_RUNNABLES.add(() -> {
                RenderUtil.roundedRectangle(posX - margin - nameWidth / 2, nY, nameWidth + margin * multiplier, nH, getTheme().getRound(), Color.BLACK);
            });
        }
    };
}