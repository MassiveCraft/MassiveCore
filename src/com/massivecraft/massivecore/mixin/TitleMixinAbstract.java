package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.Txt;

public abstract class TitleMixinAbstract implements TitleMixin
{	
	// Parsed
	@Override
	public boolean sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		if (titleMain != null) titleMain = Txt.parse(titleMain);
		if (titleSub != null) titleSub = Txt.parse(titleSub);
		return this.sendTitleMessage(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
}
