package net.minecraft.client.gui.inventory;

import cn.hackedmc.apotheosis.Client;
import cn.hackedmc.apotheosis.module.impl.player.Stealer;
import cn.hackedmc.apotheosis.module.impl.render.SniperOverlay;
import cn.hackedmc.apotheosis.util.render.StencilUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.nio.IntBuffer;

public class GuiChest extends GuiContainer {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    private final IInventory lowerChestInventory;
    public String chestTitle;
    public int lastX, lastY;
    public static StencilUtil firstItem = new StencilUtil();
    public boolean isChest, isSilent;
    /**
     * window height is calculated with these values; the more rows, the heigher
     */
    private final int inventoryRows;

    public GuiChest(final IInventory upperInv, final IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        final int i = 222;
        final int j = i - 108;

        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = j + this.inventoryRows * 18;
        /*
        chestTitle = lowerChestInventory.getDisplayName().getUnformattedText();
        Stealer.isChest = isChest = chestTitle.equals(StatCollector.translateToLocal("container.chest")) || chestTitle.equals(StatCollector.translateToLocal("container.chestDouble")) || chestTitle.equals("LOW");
        isSilent = Client.INSTANCE.getModuleManager().get(Stealer.class).isEnabled() &&  Client.INSTANCE.getModuleManager().get(Stealer.class).silent.getValue() && isChest;
        if (isSilent) {
            try {
                int min = org.lwjgl.input.Cursor.getMinCursorSize();
                IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
                Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
                Mouse.setNativeCursor(emptyCursor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        lastX = lastY = -1;
        firstItem.reset();

         */
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
