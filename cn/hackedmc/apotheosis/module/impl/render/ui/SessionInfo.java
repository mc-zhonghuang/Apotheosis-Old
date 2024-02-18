package cn.hackedmc.apotheosis.module.impl.render.ui;

import cn.hackedmc.apotheosis.module.impl.render.Gui;
import cn.hackedmc.apotheosis.newevent.Listener;
import cn.hackedmc.apotheosis.newevent.annotations.EventLink;
import cn.hackedmc.apotheosis.newevent.impl.render.Render2DEvent;
import cn.hackedmc.apotheosis.util.font.FontManager;
import cn.hackedmc.apotheosis.util.font.impl.rise.FontRenderer;
import cn.hackedmc.apotheosis.value.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

public class SessionInfo extends Mode<Gui> {
    public SessionInfo(String name, Gui parent) {
        super(name, parent);
    }
    @EventLink()
    public final Listener<Render2DEvent> onRender2D = event -> {
    final ScaledResolution scaledResolution = new ScaledResolution(mc);
    FontRenderer rapemasterfontmanager = (FontRenderer) FontManager.getProductSansRegular(20);
    double d0 = scaledResolution.getScaledWidth();
    double d1 = scaledResolution.getScaledHeight() + 12.0D;
    ArrayList<Integer> arraylist = new ArrayList<>();
      arraylist.add(rapemasterfontmanager.width("X: " + (int)mc.thePlayer.posX + " "));
      arraylist.add(rapemasterfontmanager.width("Y: " + (int)mc.thePlayer.posY + " "));
      arraylist.add(rapemasterfontmanager.width("Z: " + (int)mc.thePlayer.posZ + " "));
    int i = (new Color(160, 160, 160)).getRGB();
      rapemasterfontmanager.drawStringWithShadow("X", d0, d1, i);
      rapemasterfontmanager.drawStringWithShadow(": " + (int)mc.thePlayer.posX, d0 + (double)rapemasterfontmanager.width("X"), d1, Color.white.getRGB());
      rapemasterfontmanager.drawStringWithShadow("Y", d0 + (double)arraylist.get(0).intValue(), d1, i);
      rapemasterfontmanager.drawStringWithShadow(": " + (int)mc.thePlayer.posY, d0 + (double)rapemasterfontmanager.width("Y") + (double)arraylist.get(0).intValue(), d1, Color.white.getRGB());
      rapemasterfontmanager.drawStringWithShadow("Z", d0 + (double)arraylist.get(1).intValue() + (double)arraylist.get(0).intValue(), d1, i);
      rapemasterfontmanager.drawStringWithShadow(": " + (int)mc.thePlayer.posZ, d0 + (double)rapemasterfontmanager.width("Z") + (double)arraylist.get(1).intValue() + (double)arraylist.get(0).intValue(), d1, Color.white.getRGB());
    d1 -= rapemasterfontmanager.height() + 2.0D;
      rapemasterfontmanager.drawStringWithShadow("Fps", d0, d1, (new Color(160, 160, 160)).getRGB());
      rapemasterfontmanager.drawStringWithShadow(": " + Minecraft.getDebugFPS(), d0 + (double)rapemasterfontmanager.width("Fps"), d1, Color.white.getRGB());

   };
}
