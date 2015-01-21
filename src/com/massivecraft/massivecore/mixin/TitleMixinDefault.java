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
	public boolean sendTitleMessage(Object watcherObject, int fadeIn, int stay, int fadeOut, String mainTitle, String subTitle)
	{
		// Get the player
		Player player = IdUtil.getPlayer(watcherObject);
		if (player == null) return false;
		
		// If we don't send any message (empty is ok) we might end up displaying old messages.
		if (subTitle == null)	subTitle = "";
		if (mainTitle == null)	mainTitle = "";
		
		return TitleUtil.sendTitle(player, fadeIn, stay, fadeOut, mainTitle, subTitle);
	}

	@Override
	public boolean isTitlesAvailable()
	{
		return TitleUtil.isAvailable();
	}

}
