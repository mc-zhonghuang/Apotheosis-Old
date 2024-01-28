package cn.hackedmc.alexander.module.impl.render;


import cn.hackedmc.alexander.newevent.annotations.EventLink;
import cn.hackedmc.alexander.newevent.impl.render.Render2DEvent;
import cn.hackedmc.alexander.util.render.RenderUtil;
import cn.hackedmc.alexander.module.Module;
import cn.hackedmc.alexander.module.api.Category;
import cn.hackedmc.alexander.module.api.ModuleInfo;
import cn.hackedmc.alexander.newevent.Listener;
import cn.hackedmc.alexander.util.vector.Vector2d;
import cn.hackedmc.alexander.util.vector.Vector2f;
import cn.hackedmc.alexander.value.impl.BooleanValue;
import cn.hackedmc.alexander.value.impl.DragValue;
import net.minecraft.client.Minecraft;

import java.awt.*;

@ModuleInfo(name = "module.render.fpscounter.name", description = "module.render.fpscounter.description", category = Category.RENDER)
public final class FPSCounter extends Module  {

    private final BooleanValue showTitle = new BooleanValue("Title", this, false);
    private final DragValue position = new DragValue("Position", this, new Vector2d(200, 200));

    private final Vector2f scale = new Vector2f(RenderUtil.GENERIC_SCALE, RenderUtil.GENERIC_SCALE);

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {

        Vector2d position = this.position.position;

        final String titleString = showTitle.getValue() ? "FPS " : "";
        final String fpsString = Minecraft.getDebugFPS() + "";

        final float titleWidth = nunitoNormal.width(titleString);
        scale.x = titleWidth + nunitoNormal.width(fpsString);

        NORMAL_POST_RENDER_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(position.x, position.y, scale.x + 6, scale.y - 1, getTheme().getRound(), getTheme().getBackgroundShade());

            this.position.setScale(new Vector2d(scale.x + 6, scale.y - 1));

            final double textX = position.x + 3.0F;
            final double textY = position.y + scale.y / 2.0F - nunitoNormal.height() / 4.0F;
            nunitoNormal.drawStringWithShadow(titleString, textX, textY, getTheme().getFirstColor().getRGB());
            nunitoNormal.drawStringWithShadow(fpsString, textX + titleWidth, textY, Color.WHITE.getRGB());
        });

        NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.roundedRectangle(position.x, position.y + 0.5, scale.x + 6, scale.y - 1, getTheme().getRound(), Color.BLACK));
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> RenderUtil.roundedRectangle(position.x, position.y, scale.x + 6, scale.y - 1, getTheme().getRound() + 1, getTheme().getDropShadow()));
    };
}
