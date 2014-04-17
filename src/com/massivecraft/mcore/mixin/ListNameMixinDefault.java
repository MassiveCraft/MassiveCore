package com.massivecraft.mcore.mixin;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.util.SenderUtil;

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
	public String getListName(String senderId)
	{
		if (senderId == null) return null;
		
		// Try Our Map
		String ret = this.idToListName.get(senderId);
		
		// Try Bukkit
		if (ret == null)
		{
			Player player = SenderUtil.getPlayer(senderId);
			if (player != null)
			{
				ret = player.getPlayerListName(); 
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
	public void setListName(String senderId, String listName)
	{
		if (listName == null)
		{
			this.idToListName.remove(senderId);
		}
		else
		{
			this.idToListName.put(senderId, listName);
		}
		
		Player player = SenderUtil.getPlayer(senderId);
		if (player == null) return;
		player.setPlayerListName(this.getListName(senderId));
	}
	
}