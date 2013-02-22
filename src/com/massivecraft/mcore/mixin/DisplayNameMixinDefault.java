package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DisplayNameMixinDefault extends DisplayNameMixinAbstract
{
	public final static ChatColor DEFAULT_COLOR = ChatColor.WHITE;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DisplayNameMixinDefault i = new DisplayNameMixinDefault();
	public static DisplayNameMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Map<String, String> idToDisplayName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getDisplayName(String senderId)
	{
		if (senderId == null) return null;
		
		// Try Our Map
		String ret = this.idToDisplayName.get(senderId);
		
		// Try Bukkit
		if (ret == null)
		{
			Player player = Bukkit.getPlayerExact(senderId);
			if (player != null)
			{
				ret = player.getDisplayName(); 
			}
		}
		
		// Try Fixed Id
		if (ret == null)
		{
			ret = Mixin.tryFix(senderId);
		}
		
		// Ensure Colored
		if (ChatColor.stripColor(ret).equals(ret))
		{
			ret = DEFAULT_COLOR.toString()+ret;
		}
		
		return ret;
	}

	@Override
	public void setDisplayName(String senderId, String displayName)
	{
		if (displayName == null)
		{
			this.idToDisplayName.remove(senderId);
		}
		else
		{
			this.idToDisplayName.put(senderId, displayName);
		}
		
		Player player = Bukkit.getPlayerExact(senderId);
		if (player == null) return;
		player.setDisplayName(this.getDisplayName(senderId));
	}

}