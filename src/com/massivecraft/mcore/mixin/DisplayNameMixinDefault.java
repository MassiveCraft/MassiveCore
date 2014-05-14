package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.util.IdUtil;

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
	public String getDisplayName(Object senderObject)
	{
		String senderId = IdUtil.getId(senderObject);
		if (senderId == null) return null;
		
		// Our Map
		String ret = this.idToDisplayName.get(senderId);
		
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
			ret = DEFAULT_COLOR.toString()+ret;
		}
		
		return ret;
	}

	@Override
	public void setDisplayName(Object senderObject, String displayName)
	{
		String senderId = IdUtil.getId(senderObject);
		if (senderId == null) return;
		
		if (displayName == null)
		{
			this.idToDisplayName.remove(senderId);
		}
		else
		{
			this.idToDisplayName.put(senderId, displayName);
		}
		
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return;
		player.setDisplayName(this.getDisplayName(senderObject));
	}

}