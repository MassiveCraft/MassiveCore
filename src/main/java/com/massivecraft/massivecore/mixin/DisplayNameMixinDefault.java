package com.massivecraft.massivecore.mixin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.IdUtil;

public class DisplayNameMixinDefault extends DisplayNameMixinAbstract
{
	public final static ChatColor DEFAULT_COLOR = ChatColor.WHITE;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DisplayNameMixinDefault i = new DisplayNameMixinDefault();
	public static DisplayNameMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getDisplayName(Object senderObject, Object watcherObject)
	{
		String senderId = IdUtil.getId(senderObject);
		if (senderId == null) return null;
		
		// Ret
		String ret = null;
		
		// Bukkit
		if (ret == null)
		{
			Player player = IdUtil.getPlayer(senderObject);
			if (player != null)
			{
				ret = player.getDisplayName(); 
			}
		}
		
		// Fixed Name
		if (ret == null)
		{
			ret = IdUtil.getName(senderObject);
		}
		
		// Id Fallback
		if (ret == null)
		{
			ret = senderId;
		}
		
		// Ensure Colored
		if (ChatColor.stripColor(ret).equals(ret))
		{
			ret = DEFAULT_COLOR.toString() + ret;
		}
		
		return ret;
	}

}