package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.util.IdUtil;

public class ListNameMixinDefault extends ListNameMixinAbstract
{
	public final static ChatColor DEFAULT_COLOR = ChatColor.WHITE;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ListNameMixinDefault i = new ListNameMixinDefault();
	public static ListNameMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Map<String, String> idToListName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getListName(Object senderObject)
	{
		String senderId = IdUtil.getId(senderObject);
		if (senderId == null) return null;
		
		// Our Map
		String ret = this.idToListName.get(senderId);
		
		// Bukkit
		if (ret == null)
		{
			Player player = IdUtil.getPlayer(senderObject);
			if (player != null)
			{
				ret = player.getPlayerListName(); 
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
	public void setListName(Object senderObject, String listName)
	{
		String senderId = IdUtil.getId(senderObject);
		if (senderId == null) return;
		
		if (listName == null)
		{
			this.idToListName.remove(senderId);
		}
		else
		{
			this.idToListName.put(senderId, listName);
		}
		
		Player player = IdUtil.getPlayer(senderObject);
		if (player == null) return;
		player.setPlayerListName(this.getListName(senderObject));
	}
	
}