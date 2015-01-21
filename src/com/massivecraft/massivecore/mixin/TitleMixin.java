package com.massivecraft.massivecore.mixin;

public interface TitleMixin
{
	// Abstract
	public boolean sendTitleMessage(Object watcherObject, int fadeIn, int stay, int fadeOut, String mainTitle, String subTitle);
	
	// Parsed
	public boolean sendTitleMsg(Object watcherObject, int fadeIn, int stay, int fadeOut, String mainTitle, String subTitle);
	
	// Available
	public boolean isTitlesAvailable();
}
