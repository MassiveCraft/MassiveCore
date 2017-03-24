package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

public class MixinVisibility extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinVisibility d = new MixinVisibility();
	private static MixinVisibility i = d;
	public static MixinVisibility get() { return i; }
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public boolean isVisible(Object watcheeObject)
	{
		// The Bukkit API is not about general visibility.
		// It only handles player to player visibility.
		// With this in mind we loop to do some sort of approximation.
		// If there is any other player who can not see then we are not visible.
		// This is for the sake of security. Rather hide than display secret information.
		
		Player pwatchee = IdUtil.getPlayer(watcheeObject);
		if (pwatchee == null) return true;
		
		for (Player pwatcher : MUtil.getOnlinePlayers())
		{
			if (pwatchee.equals(pwatcher)) continue;
			if (pwatcher.canSee(pwatchee)) continue;
			return false;
		}
		
		return true;
	}
	
	public boolean isVisible(Object watcheeObject, Object watcherObject)
	{
		Player pwatchee = IdUtil.getPlayer(watcheeObject);
		Player pwatcher = IdUtil.getPlayer(watcherObject);

		if (pwatchee == null) return true;
		if (pwatcher == null) return true;

		return pwatcher.canSee(pwatchee);
	}

}
