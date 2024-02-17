package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import static cn.hackedmc.apotheosis.util.interfaces.InstanceAccess.mc;

public class ScaledResolution {
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(final Minecraft p_i46445_1_) {
        this.scaledWidth = p_i46445_1_.displayWidth;
        this.scaledHeight = p_i46445_1_.displayHeight;
        this.scaleFactor = 1;
        final boolean flag = p_i46445_1_.isUnicode();
        int i = p_i46445_1_.gameSettings == null ? 2 : p_i46445_1_.gameSettings.guiScale;

        if (i == 0) {
            i = 1000;
        }

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }

        this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }
    public int getScaledWidthStatic(Minecraft minecraft) {
        switch (mc.gameSettings.guiScale) {
            case 0:
                return getScaledWidth() * 2;
            case 1:
                return (int) (getScaledWidth() * 0.5);
            case 3:
                return (int) (getScaledWidth() * 1.5);
            default:
                return getScaledWidth();
        }
    }

    public int getScaledHeightStatic(Minecraft minecraft) {
        switch (mc.gameSettings.guiScale) {
            case 0:
                return getScaledHeight() * 2;
            case 1:
                return (int) (getScaledHeight() * 0.5);
            case 3:
                return (int) (getScaledHeight() * 1.5);
            default:
                return getScaledHeight();
        }
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
