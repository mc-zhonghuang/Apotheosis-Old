package cn.hackedmc.apotheosis.module.impl.player;

import cn.hackedmc.apotheosis.api.Rise;
import cn.hackedmc.apotheosis.component.impl.player.GUIDetectionComponent;
import cn.hackedmc.apotheosis.module.Module;
import cn.hackedmc.apotheosis.module.api.Category;
import cn.hackedmc.apotheosis.module.api.ModuleInfo;
import cn.hackedmc.apotheosis.module.impl.exploit.Disabler;
import cn.hackedmc.apotheosis.module.impl.movement.InventoryMove;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.Priorities;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.motion.PreMotionEvent;
import cn.hackedmc.apotheosis.newevent.impl.packet.PacketSendEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.ChestRenderEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.newevent.impl.render.Render3DEvent;
import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.math.MathUtil;
import cn.hackedmc.apotheosis.util.player.ItemUtil;
import cn.hackedmc.apotheosis.util.render.RenderUtil;
import cn.hackedmc.apotheosis.util.render.Stencil;
import cn.hackedmc.apotheosis.util.render.StencilUtil;
import cn.hackedmc.apotheosis.util.shader.RiseShaders;
import cn.hackedmc.apotheosis.util.shader.base.RiseShaderProgram;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.value.impl.BooleanValue;
import cn.hackedmc.apotheosis.value.impl.BoundsNumberValue;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import util.time.StopWatch;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;


@Rise
@ModuleInfo(name = "module.player.stealer.name", description = "module.player.stealer.description", category = Category.PLAYER)
public class Stealer extends Module {
    public static Stealer INSTANCE;
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 100, 150, 0, 500, 50);
    public final BooleanValue silent = new BooleanValue("Silent", this, false);
    private final BooleanValue ignoreTrash = new BooleanValue("Ignore Trash", this, true);
    private final StopWatch stopwatch = new StopWatch();
    public BlockPos blockPos;
    public long showTime;
    public BlockPos animatedPos;
    private long nextClick;
    private int lastClick;
    private int lastSteal;

    private GuiChest guiChest;
    private TileEntityChest chest;
    private Framebuffer framebuffer = new Framebuffer(1, 1, false);

    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C08PacketPlayerBlockPlacement) {
            final C08PacketPlayerBlockPlacement wrapped = (C08PacketPlayerBlockPlacement) packet;

            if (mc.theWorld.getBlockState(wrapped.getPosition()).getBlock() instanceof BlockChest)
                blockPos = wrapped.getPosition();
        }
    };

    @EventLink(value = Priorities.VERY_LOW)
    private final Listener<Render2DEvent> onRender2D = event -> {
        if (this.silent.getValue() && this.guiChest != null && this.chest != null && (blockPos != null || animatedPos != null)) {
            final int length = chest.getSizeInventory() / 9;
            framebuffer = RenderUtil.createFrameBuffer(framebuffer, true);
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(false);
            GlStateManager.enableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();

            for (int yi = 1; yi <= length; yi++) {
                for (int xi = 1; xi <= 9; xi++) {
                    final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get((yi - 1) * 9 + xi - 1).getStack();

                    if (itemStack != null) {
                        mc.getRenderItem().renderItemIntoGUI3D(itemStack, ((xi - 1) * 22) + 4, ((yi - 1) * 22) + 4);
                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (xi - 1) * 22 + 4, (yi - 1) * 22 + 4, null);
                    }
                }
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            framebuffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(false);
        }
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        if (!silent.getValue() || (blockPos == null && animatedPos == null) || (!(mc.currentScreen instanceof GuiChest) && animatedPos == null)) {
            showTime = System.currentTimeMillis();

            return;
        }

        mc.theWorld.loadedTileEntityList.forEach(entity -> {
            if (entity instanceof TileEntityChest) {
                this.chest = (TileEntityChest) entity;

                if (blockPos != null) {
                    this.guiChest = (GuiChest) mc.currentScreen;

                    if (chest.getPos().equals(blockPos)) {
                        final int length = chest.getSizeInventory() / 9;
                        final RenderManager renderManager = mc.getRenderManager();

                        final double posX = (blockPos.getX() + 0.5) - renderManager.renderPosX;
                        final double posY = blockPos.getY() - renderManager.renderPosY;
                        final double posZ = (blockPos.getZ() + 0.5) - renderManager.renderPosZ;

                        GL11.glPushMatrix();
                        GL11.glTranslated(posX, posY, posZ);
                        GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                        GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                        GL11.glScaled(Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.min((System.currentTimeMillis() - showTime) / 10000.0, 0.015));

                        glDisable(GL_DEPTH_TEST);
                        glDepthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

                        final int x = -99;
                        final int y = -105 - (Math.round(length / 2F) * 22);
                        final int width = 200;
                        final int height = length * 22 + 2;

                        RenderUtil.roundedRectangle(x, y, width, height, 6, new Color(0, 0, 0, 140));

                        for (int yi = 1; yi <= length; yi++) {
                            for (int xi = 1; xi <= 9; xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get((yi - 1) * 9 + xi - 1).getStack();

                                RenderUtil.roundedRectangle(x + ((xi - 1) * 22) + 2, y + ((yi - 1) * 22) + 2, 20, 20, 3, new Color(255, 255, 255, 76));
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, ((xi - 1) * 22) + 4, ((yi - 1) * 22) + 4);
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (xi - 1) * 22 + 4, (yi - 1) * 22 + 4, null);
//                                }
                            }
                        }

                        UI_BLOOM_RUNNABLES.add(() -> {
                            GL11.glPushMatrix();
                            GL11.glTranslated(posX, posY, posZ);
                            GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                            GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                            GL11.glScaled(Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.min((System.currentTimeMillis() - showTime) / 10000.0, 0.015));

                            RenderUtil.roundedRectangle(x, y, width, height, 6, new Color(0, 0, 0, 140));

                            GL11.glPopMatrix();
                        });

                        NORMAL_BLUR_RUNNABLES.add(() -> {
                            GL11.glPushMatrix();
                            GL11.glTranslated(posX, posY, posZ);
                            GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                            GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                            GL11.glScaled(Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.max((showTime - System.currentTimeMillis()) / 10000.0, -0.015), Math.min((System.currentTimeMillis() - showTime) / 10000.0, 0.015));

                            Stencil.write(false);
                            GlStateManager.translate(0, 0, -0.001);
                            for (int yi = 1; yi <= length; yi++) {
                                for (int xi = 1; xi <= 9; xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get((yi - 1) * 9 + xi - 1).getStack();

                                    RenderUtil.roundedRectangle(x + ((xi - 1) * 22) + 2, y + ((yi - 1) * 22) + 2, 20, 20, 3, Color.BLACK);
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, ((xi - 1) * 22) + 4, ((yi - 1) * 22) + 4);
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (xi - 1) * 22 + 4, (yi - 1) * 22 + 4, null);
//                                }
                                }
                            }
                            GlStateManager.translate(0, 0, 0.001);
                            Stencil.erase(true);
                            StencilUtil.bindReadStencilBuffer(0);
                            RenderUtil.roundedRectangle(x, y, width, height, 6, Color.BLACK);
                            Stencil.dispose();

                            GL11.glPopMatrix();
                        });

//                        GlStateManager.translate(0, 0, -2D);
                        GlStateManager.bindTexture(framebuffer.framebufferTexture);
                        RiseShaderProgram.drawQuad(x, y, framebuffer.framebufferTextureWidth / (double) mc.scaledResolution.getScaleFactor(), framebuffer.framebufferTextureHeight / (double) mc.scaledResolution.getScaleFactor());

//                        GlStateManager.enableRescaleNormal();
//                        RenderHelper.enableGUIStandardItemLighting();
//
//                        for (int yi = 1;yi <= length;yi++) {
//                            for (int xi = 1;xi <= 9;xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get(yi * xi - 1).getStack();
//
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, x + ((xi - 1) * 18), y + ((yi - 1) * 18));
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, x + ((xi - 1) * 18), y + ((yi - 1) * 18), null);
//                                }
//                            }
//                        }
//
//                        RenderHelper.disableStandardItemLighting();
//                        GlStateManager.disableRescaleNormal();

                        glDepthMask(true);
                        glEnable(GL_DEPTH_TEST);
                        glDisable(GL_BLEND);

                        GL11.glPopMatrix();
                    }
                } else if (guiChest != null) {
                    // GL11.glScaled(Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.max((showTime - System.currentTimeMillis() + 150) / 10000.0, 0));
                    if (System.currentTimeMillis() - showTime <= 150) {
                        if (chest.getPos().equals(animatedPos)) {
                            final int length = chest.getSizeInventory() / 9;
                            final RenderManager renderManager = mc.getRenderManager();

                            final double posX = (animatedPos.getX() + 0.5) - renderManager.renderPosX;
                            final double posY = animatedPos.getY() - renderManager.renderPosY;
                            final double posZ = (animatedPos.getZ() + 0.5) - renderManager.renderPosZ;

                            GL11.glPushMatrix();
                            GL11.glTranslated(posX, posY, posZ);
                            GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                            GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                            GL11.glScaled(Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.max((showTime - System.currentTimeMillis() + 150) / 10000.0, 0));

                            glDisable(GL_DEPTH_TEST);
                            glDepthMask(false);
                            GlStateManager.enableBlend();
                            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

                            final int x = -99;
                            final int y = -105 - (Math.round(length / 2F) * 22);
                            final int width = 200;
                            final int height = length * 22 + 2;

                            RenderUtil.roundedRectangle(x, y, width, height, 6, new Color(0, 0, 0, 140));

                            for (int yi = 1; yi <= length; yi++) {
                                for (int xi = 1; xi <= 9; xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get((yi - 1) * 9 + xi - 1).getStack();

                                    RenderUtil.roundedRectangle(x + ((xi - 1) * 22) + 2, y + ((yi - 1) * 22) + 2, 20, 20, 3, new Color(255, 255, 255, 76));
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, ((xi - 1) * 22) + 4, ((yi - 1) * 22) + 4);
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (xi - 1) * 22 + 4, (yi - 1) * 22 + 4, null);
//                                }
                                }
                            }

                            UI_BLOOM_RUNNABLES.add(() -> {
                                GL11.glPushMatrix();
                                GL11.glTranslated(posX, posY, posZ);
                                GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                                GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                                GL11.glScaled(Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.max((showTime - System.currentTimeMillis() + 150) / 10000.0, 0));

                                RenderUtil.roundedRectangle(x, y, width, height, 6, new Color(0, 0, 0, 140));

                                GL11.glPopMatrix();
                            });

                            NORMAL_BLUR_RUNNABLES.add(() -> {
                                GL11.glPushMatrix();
                                GL11.glTranslated(posX, posY, posZ);
                                GL11.glRotated(-mc.getRenderManager().playerViewY, 0F, 1F, 0F);
                                GL11.glRotated(-mc.getRenderManager().playerViewX, -1F, 0F, 0F);
                                GL11.glScaled(Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.min((System.currentTimeMillis() - showTime - 150) / 10000.0, 0), Math.max((showTime - System.currentTimeMillis() + 150) / 10000.0, 0));

                                Stencil.write(false);
                                GlStateManager.translate(0, 0, -0.001);
                                for (int yi = 1; yi <= length; yi++) {
                                    for (int xi = 1; xi <= 9; xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get((yi - 1) * 9 + xi - 1).getStack();

                                        RenderUtil.roundedRectangle(x + ((xi - 1) * 22) + 2, y + ((yi - 1) * 22) + 2, 20, 20, 3, Color.BLACK);
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, ((xi - 1) * 22) + 4, ((yi - 1) * 22) + 4);
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, (xi - 1) * 22 + 4, (yi - 1) * 22 + 4, null);
//                                }
                                    }
                                }
                                GlStateManager.translate(0, 0, 0.001);
                                Stencil.erase(true);
                                StencilUtil.bindReadStencilBuffer(0);
                                RenderUtil.roundedRectangle(x, y, width, height, 6, Color.BLACK);
                                Stencil.dispose();

                                GL11.glPopMatrix();
                            });

//                        GlStateManager.translate(0, 0, -2D);
                            GlStateManager.bindTexture(framebuffer.framebufferTexture);
                            RiseShaderProgram.drawQuad(x, y, framebuffer.framebufferTextureWidth / (double) mc.scaledResolution.getScaleFactor(), framebuffer.framebufferTextureHeight / (double) mc.scaledResolution.getScaleFactor());

//                        GlStateManager.enableRescaleNormal();
//                        RenderHelper.enableGUIStandardItemLighting();
//
//                        for (int yi = 1;yi <= length;yi++) {
//                            for (int xi = 1;xi <= 9;xi++) {
//                                final ItemStack itemStack = guiChest.inventorySlots.inventorySlots.get(yi * xi - 1).getStack();
//
//                                if (itemStack != null) {
//                                    mc.getRenderItem().renderItemIntoGUI3D(itemStack, x + ((xi - 1) * 18), y + ((yi - 1) * 18));
//                                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, x + ((xi - 1) * 18), y + ((yi - 1) * 18), null);
//                                }
//                            }
//                        }
//
//                        RenderHelper.disableStandardItemLighting();
//                        GlStateManager.disableRescaleNormal();

                            glDepthMask(true);
                            glEnable(GL_DEPTH_TEST);
                            glDisable(GL_BLEND);

                            GL11.glPopMatrix();
                        }
                    } else {
                        animatedPos = null;
                    }
                }
            }
        });
    };

    @EventLink
    private final Listener<ChestRenderEvent> onChestRender = event ->{
        if (silent.getValue() && blockPos != null) {
            final GuiContainer container = event.getScreen();
            if (container instanceof GuiChest) {
                event.setCancelled();
                if (InventoryMove.INSTANCE.isEnabled()) {
                    mc.inGameHasFocus = true;
                    mc.mouseHelper.grabMouseCursor();
                    mc.setIngameFocus();
                }
            }
        }
    };

    @EventLink()
    public final Listener<PreMotionEvent> onPreMotionEvent = event -> {
        /*
        if (!isChest && onlychest.getValueState())
            return;
            
         */
        if (mc.currentScreen instanceof GuiChest) {
            final ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;

            if (GUIDetectionComponent.inGUI() || !this.stopwatch.finished(this.nextClick)) {
                return;
            }

            this.lastSteal++;

            for (int i = 0; i < container.inventorySlots.size(); i++) {
                final ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);

                if (stack == null || this.lastSteal <= 1) {
                    continue;
                }

                if (this.ignoreTrash.getValue() && !ItemUtil.useful(stack)) {
                    continue;
                }

                this.nextClick = Math.round(MathUtil.getRandom(this.delay.getValue().intValue(), this.delay.getSecondValue().intValue()));
                if (Disabler.INSTANCE.isEnabled() && Disabler.INSTANCE.watchdog.getValue()) {
                    int emptySlot = -1;
                    for (int index = 0; index < mc.thePlayer.inventory.mainInventory.length; index++) {
                        if (mc.thePlayer.inventory.mainInventory[index] == null) {
                            emptySlot = index;
                            break;
                        }
                    }

                    if (emptySlot != -1) {
                        mc.playerController.windowClick(container.windowId, i, 0, 0, mc.thePlayer);
                        if (emptySlot <= 8) {
                            mc.playerController.windowClick(container.windowId, emptySlot + 54, 0, 0, mc.thePlayer);
                        } else {
                            mc.playerController.windowClick(container.windowId, emptySlot + 18, 0, 0, mc.thePlayer);
                        }
                    }
                } else {
                    mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                }
                this.stopwatch.reset();
                this.lastClick = 0;
                if (this.nextClick > 0) return;
            }

            this.lastClick++;

            if (this.lastClick > 1) {
                mc.thePlayer.closeScreen();
            }
        } else {
            this.lastClick = 0;
            this.lastSteal = 0;
        }
    };

    public Stealer() {
        INSTANCE = this;
    }
}