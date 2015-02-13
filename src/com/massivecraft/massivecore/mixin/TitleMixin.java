package com.massivecraft.massivecore.mixin;

public interface TitleMixin
{
	// Abstract
	public boolean sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub);
	
	// Parsed
	public boolean sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub);
	
	// Available
	public boolean isTitlesAvailable();
}
