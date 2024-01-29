package cn.hackedmc.apotheosis.script.api.wrapper.impl.event.impl;

import cn.hackedmc.apotheosis.newevent.impl.render.Render3DEvent;
import cn.hackedmc.apotheosis.script.api.wrapper.impl.event.ScriptEvent;

public class ScriptRender3DEvent extends ScriptEvent<Render3DEvent>
{
	public ScriptRender3DEvent(Render3DEvent wrappedEvent)
	{
		super(wrappedEvent);
	}

	public float getPartialTicks() {
		return this.wrapped.getPartialTicks();
	}

	@Override
	public String getHandlerName()
	{
		return "onRender3D";
	}
}
