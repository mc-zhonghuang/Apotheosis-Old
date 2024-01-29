package cn.hackedmc.apotheosis.component.impl.player;

import cn.hackedmc.apotheosis.component.Component;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.inventory.SyncCurrentItemEvent;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreUpdateEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.animation.Animation;
import cn.hackedmc.apotheosis.util.animation.Easing;
import cn.hackedmc.apotheosis.util.font.Font;
import cn.hackedmc.apotheosis.util.font.FontManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public final class SlotComponent extends Component {

    private static final Animation animation = new Animation(Easing.EASE_OUT_ELASTIC, 250);
    private static boolean render;

    private final Font productSansLight = FontManager.getProductSansLight(18);
    private final Font productSansMedium = FontManager.getProductSansMedium(18);

    public static void setSlot(final int slot, final boolean render) {
        if (slot < 0 || slot > 8) {
            return;
        }

        mc.thePlayer.inventory.alternativeCurrentItem = slot;
        mc.thePlayer.inventory.alternativeSlot = true;
        SlotComponent.render = render;
    }

    public static void setSlot(final int slot) {
        setSlot(slot, true);
    }

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<Render2DEvent> onRender2D = event -> {

        final ScaledResolution scaledResolution = event.getScaledResolution();
        final double destinationY = render && mc.thePlayer.inventory.alternativeSlot &&
                (mc.thePlayer.inventory.alternativeCurrentItem != mc.thePlayer.inventory.currentItem || getItemStack() == null || getItemStack().getItem() instanceof ItemBlock) &&
                mc.currentScreen == null ? scaledResolution.getScaledHeight() - 90 : scaledResolution.getScaledHeight();
        animation.run(destinationY);
        animation.setDuration(1000);

        if (!render && animation.isFinished()) {
            return;
        }

        final ItemStack itemStack = getItemStack();
/*
        if (itemStack != null) {
            final String stackSize = String.valueOf(itemStack.stackSize);
            final Item item = itemStack.getItem();

            final int halfWidth = scaledResolution.getScaledWidth() / 2;
            final double y = animation.getValue();

            // TODO: Convert some of those to constants
            final int edgeOffset = 3;
            final float amountWidth = productSansLight.width("Amount:") + 2;
            final double width = 16 + edgeOffset * 2 + (item instanceof ItemBlock ? amountWidth + productSansMedium.width(stackSize) + 2 + edgeOffset : 0);
            final double height = 22;
          final double x = halfWidth - width / 2.0F;
            final double fontY = y + height / 2.0F - this.productSansLight.height() / 2.0F + 3;
            final int alpha = MathHelper.clamp_int((int) (255 * (float) (scaledResolution.getScaledHeight() - animation.getValue()) / 90.0F), 0, 255);
            if (alpha > 0) {
                NORMAL_RENDER_RUNNABLES.add(() -> {
                    RenderUtil.roundedRectangle(x, y, width, height, getTheme().getRound(), getTheme().getBackgroundShade());

                    RenderUtil.renderItemIcon(x + edgeOffset, y + edgeOffset, itemStack);

                    if (item instanceof ItemBlock) {
                        this.productSansLight.drawString("Amount:", x + edgeOffset * 2 + 16, fontY, Color.WHITE.getRGB());
                        this.productSansMedium.drawString("" + stackSize, x + edgeOffset * 2 + 16 + amountWidth, fontY, getTheme().getFirstColor().getRGB());
                    }
                });

                NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.roundedRectangle(x, y, width, height, getTheme().getRound(), ColorUtil.withAlpha(Color.BLACK, alpha)));
                NORMAL_POST_BLOOM_RUNNABLES.add(() -> RenderUtil.roundedRectangle(x, y, width, height, getTheme().getRound() + 0.5F, getTheme().getDropShadow()));
            }
        }

 */
    };

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<SyncCurrentItemEvent> onSyncItem = event -> {
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;

        event.setSlot(inventoryPlayer.alternativeSlot ? inventoryPlayer.alternativeCurrentItem : inventoryPlayer.currentItem);
    };

    @EventLink(value = Priorities.VERY_HIGH)
    public final Listener<PreUpdateEvent> onPreUpdate = event -> {
        mc.thePlayer.inventory.alternativeSlot = false;
    };

    public static ItemStack getItemStack() {
        return (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null ? null : mc.thePlayer.inventoryContainer.getSlot(getItemIndex() + 36).getStack());
    }

    public static int getItemIndex() {
        final InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        return inventoryPlayer.alternativeSlot || !animation.isFinished() ? inventoryPlayer.alternativeCurrentItem : inventoryPlayer.currentItem;
    }
}