package cn.hackedmc.apotheosis.module.impl.render;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.impl.DragValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

@ModuleInfo(name = "module.combat.inventoryhud.name", description = "module.combat.inventoryhud.description", category = Category.RENDER)
public class InventoryHUD extends Module {
    private final Font productSans = FontManager.getProductSansRegular(20);
    private final DragValue position = new DragValue("", this, new Vector2d(200, 100), true);
    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
        double startY = -12.0;
        double x = this.position.position.x;
        double y = this.position.position.y;
        Color logoColor = this.getTheme().getFirstColor();
        Color rightColor = this.getTheme().getSecondColor();
        // blur
        NORMAL_BLUR_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(x + 2F, y + startY,170F, 73F,0.2f , Color.BLACK);
        });
        //shadow
        NORMAL_POST_BLOOM_RUNNABLES.add(() -> {
            RenderUtil.roundedRectangle(x + 2F, y + startY,170F, 73F,0.2f ,Color.BLACK);
            RenderUtil.drawRoundedGradientRect(x + 2F, y + startY,170F, 3F,0.2f ,logoColor,rightColor,false);
            RenderUtil.drawRoundedGradientRect(x + 2F, y + startY,170F, 3F,0.2f ,rightColor,logoColor,false);
        });
        RenderUtil.roundedRectangle(x + 2F, y + startY,170F, 73F,0.2f , new Color(0,0,0,50));
          //font 必须一起渲染 不然boom
        NORMAL_RENDER_RUNNABLES.add(() -> {
            RenderUtil.drawRoundedGradientRect(x + 2F, y + startY,170F, 3F,0.2f ,rightColor,logoColor,false);
            RenderUtil.drawRoundedGradientRect(x + 2F, y + startY,170F, 3F,0.2f ,logoColor,rightColor,false);
            productSans.drawString("Inventory", x + 6f, y + (startY +  20.0F) -13,logoColor.getRGB());
            GlStateManager.resetColor();
          //物品显示
            RenderHelper.enableGUIStandardItemLighting();
            renderInv(9, 17, (int) (x + 6), (int) (y + 6));
            renderInv(18, 26, (int) (x + 6), (int) (y + 24));
            renderInv(27, 35, (int) (x + 6), (int) (y + 42));
            RenderHelper.disableStandardItemLighting();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
        });
    };
    private void renderInv(int slot, int endSlot, int x, int y) {
        int xOffset = x;
        for (int i = slot; i <= endSlot; i++) {
            xOffset += 18;
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null) {
                continue;
            }
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xOffset - 18, y);
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, xOffset - 18, y);
        }
    }
}