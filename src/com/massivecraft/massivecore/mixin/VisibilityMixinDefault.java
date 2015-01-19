package com.massivecraft.massivecore.mixin;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class VisibilityMixinDefault extends VisibilityMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static VisibilityMixinDefault i = new VisibilityMixinDefault();
	public static VisibilityMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean canSee(Object watcherObject, Object watcheeObject)
	{
		Player pwatcher = IdUtil.getPlayer(watcherObject);
		Player pwatchee = IdUtil.getPlayer(watcheeObject);
		
		if (pwatcher == null) return true;
		if (pwatchee == null) return true;
		
		return pwatcher.canSee(pwatchee);
	}
	
}
