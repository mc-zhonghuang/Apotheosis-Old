package cn.hackedmc.apotheosis.module.impl.render.ui;

import cn.hackedmc.apotheosis.module.impl.render.Gui;
import cn.hackedmc.apotheosis.module.impl.render.TargetInfo;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.font.impl.minecraft.FontRenderer;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.vector.Vector2d;
import cn.hackedmc.apotheosis.value.Mode;
import cn.hackedmc.apotheosis.value.impl.DragValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class Inventory extends Mode<Gui> {
    private Gui guiModule;

    public Inventory(String name, Gui parent) {
          super(name, parent);
    }
    double startY = -12.0;

    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {

        if (this.guiModule == null) {
            this.guiModule = this.getModule(Gui.class);
        }

        Color logoColor = this.getTheme().getFirstColor();

        //RenderUtil.roundedRectangle(guiModule.position.x + 2F, guiModule.position.y + startY + 10,   170F, 62F,0f , new Color(0,0,0,50));
        FontManager.getProductSansRegular(17).drawString("Inventory", 16f, (startY +  20.0F ) -11,logoColor.getRGB());

        GlStateManager.resetColor();
// render item
        RenderHelper.enableGUIStandardItemLighting();
        renderInv(9, 17, 6, 6);
        renderInv(18, 26, 6, 24);
        renderInv(27, 35, 6, 42);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();


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
