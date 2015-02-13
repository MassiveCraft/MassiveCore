package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.TitleUtil;

public class TitleMixinDefault extends TitleMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TitleMixinDefault i = new TitleMixinDefault();
	public static TitleMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if (player == null) return false;
		
		// If we don't send any message (empty is ok) we might end up displaying old messages.
		if (titleSub == null)	titleSub = "";
		if (titleMain == null)	titleMain = "";
		
		return TitleUtil.sendTitle(player, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}

	@Override
	public boolean isTitlesAvailable()
	{
		return TitleUtil.isAvailable();
	}

}
