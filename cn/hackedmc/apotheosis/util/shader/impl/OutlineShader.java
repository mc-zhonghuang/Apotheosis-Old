package cn.hackedmc.apotheosis.util.shader.impl;

import cn.hackedmc.apotheosis.util.interfaces.InstanceAccess;
import cn.hackedmc.apotheosis.util.shader.base.RiseShader;
import cn.hackedmc.apotheosis.util.shader.base.RiseShaderProgram;
import cn.hackedmc.apotheosis.util.shader.base.ShaderRenderType;
import cn.hackedmc.apotheosis.util.shader.base.ShaderUniforms;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class OutlineShader extends RiseShader {

    private final RiseShaderProgram shaderProgram = new RiseShaderProgram("outline.frag", "vertex.vsh");
    private Framebuffer inputFramebuffer = new Framebuffer(InstanceAccess.mc.displayWidth, InstanceAccess.mc.displayHeight, true);

    @Override
    public void run(final ShaderRenderType type, final float partialTicks, List<Runnable> runnable) {
        // Prevent rendering
        if (!Display.isVisible()) {
            return;
        }

        switch (type) {
            case CAMERA: {
                this.update();
                this.setActive(!runnable.isEmpty());

                if (this.isActive()) {
                    RendererLivingEntity.NAME_TAG_RANGE = 0;
                    RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 0;

                    this.inputFramebuffer.bindFramebuffer(true);
                    runnable.forEach(Runnable::run);
                    InstanceAccess.mc.getFramebuffer().bindFramebuffer(true);

                    RendererLivingEntity.NAME_TAG_RANGE = 64;
                    RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 32;

                    RenderHelper.disableStandardItemLighting();
                    InstanceAccess.mc.entityRenderer.disableLightmap();
                }
                break;
            }
            case OVERLAY: {
                this.setActive(this.isActive() || !runnable.isEmpty());

                if (this.isActive()) {

                    this.inputFramebuffer.bindFramebuffer(true);
                    runnable.forEach(Runnable::run);

                    // TODO: make radius and other things as a setting
                    final int radius = 1;
                    final int programId = this.shaderProgram.getProgramId();

                    InstanceAccess.mc.getFramebuffer().bindFramebuffer(true);
                    this.shaderProgram.start();

                    ShaderUniforms.uniform1i(programId, "u_texture", 0);
                    ShaderUniforms.uniform1f(programId, "u_radius", radius);
                    ShaderUniforms.uniform2f(programId, "u_texel_size", 1.0F / InstanceAccess.mc.displayWidth, 1.0F / InstanceAccess.mc.displayHeight);

                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                    GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
                    this.inputFramebuffer.bindFramebufferTexture();
                    RiseShaderProgram.drawQuad();
                    GlStateManager.disableBlend();

                    RiseShaderProgram.stop();
                }

                break;
            }
        }
    }

    @Override
    public void update() {
        this.setActive(false);

        if (InstanceAccess.mc.displayWidth != inputFramebuffer.framebufferWidth || InstanceAccess.mc.displayHeight != inputFramebuffer.framebufferHeight) {
            inputFramebuffer.deleteFramebuffer();
            inputFramebuffer = new Framebuffer(InstanceAccess.mc.displayWidth, InstanceAccess.mc.displayHeight, true);
        } else {
            inputFramebuffer.framebufferClear();
        }
    }
}