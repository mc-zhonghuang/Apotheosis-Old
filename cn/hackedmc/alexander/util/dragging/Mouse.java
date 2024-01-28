package cn.hackedmc.alexander.util.dragging;

import cn.hackedmc.alexander.util.interfaces.InstanceAccess;
import cn.hackedmc.alexander.util.vector.Vector2d;
import net.minecraft.client.gui.ScaledResolution;

public class Mouse {
    public static Vector2d getMouse() {
        final ScaledResolution scaledResolution = InstanceAccess.mc.scaledResolution;
        final int mouseX = org.lwjgl.input.Mouse.getX() * scaledResolution.getScaledWidth() / InstanceAccess.mc.displayWidth;
        final int mouseY = scaledResolution.getScaledHeight() - org.lwjgl.input.Mouse.getY() * scaledResolution.getScaledHeight() / InstanceAccess.mc.displayHeight - 1;

        return new Vector2d(mouseX, mouseY);
    }
}
