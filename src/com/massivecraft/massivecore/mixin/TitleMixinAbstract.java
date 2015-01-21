package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.Txt;

public abstract class TitleMixinAbstract implements TitleMixin
{	
	// Parsed
	@Override
	public boolean sendTitleMsg(Object watcherObject, int fadeIn, int stay, int fadeOut, String mainTitle, String subTitle)
	{
		if (mainTitle != null) mainTitle = Txt.parse(mainTitle);
		if (subTitle != null) subTitle = Txt.parse(subTitle);
		return this.sendTitleMessage(watcherObject, fadeIn, stay, fadeOut, mainTitle, subTitle);
	}
	
}
