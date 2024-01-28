package cn.hackedmc.alexander.script.api.wrapper.impl;

import cn.hackedmc.alexander.script.api.RenderAPI;
import cn.hackedmc.alexander.util.font.impl.rise.FontRenderer;

public class ScriptRiseFontRenderer extends ScriptFontRenderer<FontRenderer>
{
	public ScriptRiseFontRenderer(FontRenderer wrapped)
	{
		super(wrapped);
	}

	@Override
	public double width(String string)
	{
		return this.wrapped.width(string);
	}

	@Override
	public double height()
	{
		return this.wrapped.height();
	}

	@Override
	public void draw(String string, double x, double y, int[] color)
	{
		this.wrapped.drawString(string, x, y, RenderAPI.intArrayToColor(color).getRGB());
	}

	@Override
	public void drawCentered(String string, double x, double y, int[] color)
	{
		this.wrapped.drawCenteredString(string, x, y, RenderAPI.intArrayToColor(color).getRGB());
	}

	@Override
	public void drawWithShadow(String string, double x, double y, int[] color)
	{
		this.wrapped.drawStringWithShadow(string, x, y, RenderAPI.intArrayToColor(color).getRGB());
	}

	@Override
	public void drawCenteredWithShadow(String string, float x, float y, int[] color)
	{
		this.wrapped.drawCenteredStringWithShadow(string, x, y, RenderAPI.intArrayToColor(color).getRGB());
	}

}
