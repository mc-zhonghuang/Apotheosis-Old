package cn.hackedmc.apotheosis.module.impl.render.footprint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

@Getter
@AllArgsConstructor
public class PlayerRenderData {
    private final Entity entity;
    private final Render<Entity> render;
    public int tick;
    public PlayerRenderData build() {
        this.tick++;
        return this;
    }
}
