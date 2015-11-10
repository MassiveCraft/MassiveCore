package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.Txt;

public abstract class ActionbarMixinAbstract implements ActionbarMixin
{
	// Parsed
	@Override
	public boolean sendActionbarMsg(Object watcherObject, String message)
	{
		return this.sendActionbarMessage(watcherObject, Txt.parse(message));
	}
}
