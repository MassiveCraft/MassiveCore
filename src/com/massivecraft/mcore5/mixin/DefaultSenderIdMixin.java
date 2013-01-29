package com.massivecraft.mcore5.mixin;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.util.MUtil;
import com.massivecraft.mcore5.util.SenderUtil;

public class DefaultSenderIdMixin implements SenderIdMixin, Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DefaultSenderIdMixin i = new DefaultSenderIdMixin();
	public static DefaultSenderIdMixin get() { return i; }
	
	// -------------------------------------------- //
	// REGISTER & UNREGISTER
	// -------------------------------------------- //
	
	public void register()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, MCore.p);
	}
	
	public void unregister()
	{
		HandlerList.unregisterAll(this);
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		for (String playerName : MUtil.getPlayerDirectoryNames())
		{
			this.idToCorrectId.put(playerName, playerName);
		}
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// This map will contain all players that ever were on the server.
	// It will however not contain any senders registered through SenderUtil.
	protected Map<String, String> idToCorrectId = new ConcurrentSkipListMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		String id = event.getPlayer().getName();
		idToCorrectId.put(id, id);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: FIX
	// -------------------------------------------- //
	
	@Override
	public String reqFix(String senderId)
	{
		if (!SenderUtil.isPlayerId(senderId)) return senderId;
		return this.idToCorrectId.get(senderId);
	}
	
	@Override
	public String tryFix(String senderId)
	{
		String ret = this.reqFix(senderId);
		if (ret != null) return ret;
		return senderId;
	}

	@Override
	public boolean canFix(String senderId)
	{
		return this.reqFix(senderId) != null;
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SIMPLES
	// -------------------------------------------- //
	
	@Override
	public boolean isOnline(String senderId)
	{
		if (senderId == null) return false;
		if (SenderUtil.isPlayerId(senderId))
		{
			Player player = Bukkit.getPlayerExact(senderId);
			return player != null;
		}
		else
		{
			// Non-players must be registered for us to consider them online.
			CommandSender sender = SenderUtil.getSender(senderId);
			return sender != null;
		}
	}

	@Override
	public boolean isOffline(String senderId)
	{
		return !this.isOnline(senderId);
	}

	@Override
	public boolean hasBeenOnline(String senderId)
	{
		CommandSender sender = SenderUtil.getSender(senderId);
		if (sender != null) return true;
		return this.idToCorrectId.containsKey(senderId);
	}
	
	@Override
	public Long getLastOnline(String senderId)
	{
		if (this.isOnline(senderId)) return System.currentTimeMillis();
		
		String playerNameCC = this.reqFix(senderId);
		if (playerNameCC == null) return null;
		
		File playerFile = new File(MUtil.getPlayerDirectory(), playerNameCC+".dat");
		return playerFile.lastModified();
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SETS
	// -------------------------------------------- //

	@Override
	public Set<String> getAllSenderIds()
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		ret.addAll(SenderUtil.getIdToSender().keySet());
		ret.addAll(this.idToCorrectId.keySet());
		return ret;
	}
	
	@Override
	public Set<String> getOnlineSenderIds()
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		ret.addAll(SenderUtil.getIdToSender().keySet());
		for (Player player : Bukkit.getOnlinePlayers())
		{
			ret.add(player.getName());
		}
		return ret;
	}

	@Override
	public Set<String> getOfflineSenderIds()
	{
		Set<String> ret = this.getAllSenderIds();
		ret.removeAll(this.getOnlineSenderIds());
		return ret;
	}
}