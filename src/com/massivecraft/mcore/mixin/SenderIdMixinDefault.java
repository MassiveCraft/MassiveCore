package com.massivecraft.mcore.mixin;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore.event.MCoreSenderRegisterEvent;
import com.massivecraft.mcore.event.MCoreSenderUnregisterEvent;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.SenderUtil;
import com.massivecraft.mcore.util.Txt;

public class SenderIdMixinDefault extends SenderIdMixinAbstract implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SenderIdMixinDefault i = new SenderIdMixinDefault();
	public static SenderIdMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		long start = System.currentTimeMillis();
		
		// Create new empty sets
		this.allSenderIds = new ConcurrentSkipListMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		this.onlineSenderIds = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		this.offlineSenderIds = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		this.allPlayerIds = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		this.onlinePlayerIds = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		this.offlinePlayerIds = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		// Add online players
		for (Player player : Bukkit.getOnlinePlayers())
		{
			String id = player.getName();
			
			this.allSenderIds.put(id, id);
			this.allPlayerIds.add(id);
			
			this.onlineSenderIds.add(id);
			this.onlinePlayerIds.add(id);
		}
		
		// Add offline players
		for (String id : MUtil.getPlayerDirectoryNames())
		{
			// Check if this player was added already since it's online
			if (this.onlinePlayerIds.contains(id)) continue;
			
			this.allSenderIds.put(id, id);
			this.allPlayerIds.add(id);
			
			this.offlineSenderIds.add(id);
			this.offlinePlayerIds.add(id);
		}
		
		// Add command senders
		for (String id : SenderUtil.getIdToSender().keySet())
		{
			this.allSenderIds.put(id, id);
			this.onlineSenderIds.add(id);			
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(this, MCore.p);
		
		long end = System.currentTimeMillis();
		MCore.get().log(Txt.parse("<i>Setup of SenderIdMixinDefault took <h>%d<i>ms.", end-start));
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// The first field is a map since we use it for the "try" methods.
	protected Map<String, String> allSenderIds;
	protected Set<String> onlineSenderIds;
	protected Set<String> offlineSenderIds;
	
	protected Set<String> allPlayerIds;
	protected Set<String> onlinePlayerIds;
	protected Set<String> offlinePlayerIds;
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	protected void onOnlineChanged(CommandSender sender, boolean isOnline)
	{
		boolean isPlayer = SenderUtil.isPlayer(sender);
		String id = SenderUtil.getSenderId(sender);
		
		this.allSenderIds.put(id, id);
		if (isPlayer)
		{
			this.allPlayerIds.add(id);
		}
		
		if (isOnline)
		{
			this.onlineSenderIds.add(id);
			this.offlineSenderIds.remove(id);
			if (isPlayer)
			{
				this.onlinePlayerIds.add(id);
				this.offlinePlayerIds.remove(id);
			}
		}
		else
		{
			this.offlineSenderIds.add(id);
			this.onlineSenderIds.remove(id);
			if (isPlayer)
			{
				this.offlinePlayerIds.add(id);
				this.onlinePlayerIds.remove(id);
			}
		}
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	// We don't care if it's cancelled or not.
	// We just wan't to make sure this id is known of and can be "fixed" asap.
	// Online or not? We just use the mixin to get the actuall value.
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerLoginLowest(PlayerLoginEvent event)
	{
		boolean isOnline = Mixin.isOnline(SenderUtil.getSenderId(event.getPlayer()));
		this.onOnlineChanged(event.getPlayer(), isOnline);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerJoinLowest(PlayerJoinEvent event)
	{
		this.onOnlineChanged(event.getPlayer(), true);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.LOWEST)
	public void senderRegisterLowest(MCoreSenderRegisterEvent event)
	{
		this.onOnlineChanged(event.getSender(), true);		
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerLeaveMonitor(MCorePlayerLeaveEvent event)
	{
		this.onOnlineChanged(event.getPlayer(), false);
	}
	
	// Can't be cancelled
	@EventHandler(priority = EventPriority.MONITOR)
	public void senderUnregisterLowest(MCoreSenderUnregisterEvent event)
	{
		this.onOnlineChanged(event.getSender(), false);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: FIX
	// -------------------------------------------- //
	
	@Override
	public String reqFix(String senderId)
	{
		return this.allSenderIds.get(senderId);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: SETS
	// -------------------------------------------- //
	
	@Override
	public Set<String> getAllSenderIds()
	{
		return Collections.unmodifiableSet(this.allSenderIds.keySet());
	}

	@Override
	public Set<String> getOnlineSenderIds()
	{
		return Collections.unmodifiableSet(this.onlineSenderIds);
	}

	@Override
	public Set<String> getOfflineSenderIds()
	{
		return Collections.unmodifiableSet(this.offlineSenderIds);
	}

	@Override
	public Set<String> getAllPlayerIds()
	{
		return Collections.unmodifiableSet(this.allPlayerIds);
	}

	@Override
	public Set<String> getOnlinePlayerIds()
	{
		return Collections.unmodifiableSet(this.onlinePlayerIds);
	}

	@Override
	public Set<String> getOfflinePlayerIds()
	{
		return Collections.unmodifiableSet(this.offlinePlayerIds);
	}
	
}