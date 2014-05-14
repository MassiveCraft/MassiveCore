package com.massivecraft.mcore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.event.MCoreAfterPlayerRespawnEvent;
import com.massivecraft.mcore.event.MCoreAfterPlayerTeleportEvent;
import com.massivecraft.mcore.event.MCorePermissionDeniedFormatEvent;
import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore.event.MCorePlayerToRecipientChatEvent;
import com.massivecraft.mcore.event.MCoreSenderRegisterEvent;
import com.massivecraft.mcore.event.MCoreSenderUnregisterEvent;
import com.massivecraft.mcore.mixin.Mixin;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.SenderColl;
import com.massivecraft.mcore.util.FlyUtil;
import com.massivecraft.mcore.util.IdUtil;
import com.massivecraft.mcore.util.SmokeUtil;
import com.massivecraft.mcore.util.Txt;

public class EngineMainMCore extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMainMCore i = new EngineMainMCore();
	public static EngineMainMCore get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MCore.get();
	}
	
	@Override
	public void activate()
	{
		super.activate();
		MCorePlayerLeaveEvent.player2event.clear();
	}
	
	// -------------------------------------------- //
	// FLY UTIL & EVENT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void updateFly(PlayerMoveEvent event)
	{
		// If a player ...
		Player player = event.getPlayer();
		
		// ... moved from one block to another ...
		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
		
		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// ... trigger a fly update.
		FlyUtil.update(player);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void negateNoCheatPlusBug(EntityDamageEvent event)
	{
		// If a player ...
		if ( ! (event.getEntity() instanceof Player)) return;
		Player player = (Player)event.getEntity();
		
		// ... is taking fall damage ...
		if (event.getCause() != DamageCause.FALL) return;
		
		// ... within 2 seconds of flying ...
		Long lastActive = FlyUtil.getLastActive(player);
		if (lastActive == null) return;
		if (System.currentTimeMillis() - lastActive > 2000) return;
		
		// ... cancel the event.
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// RECIPIENT CHAT
	// -------------------------------------------- //
	// A system to create per recipient events.
	// It clears the recipient set so the event isn't cancelled completely.
	// It will cause non async chat events not to fire.
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void recipientChat(final AsyncPlayerChatEvent event)
	{
		// Return unless we are using the recipient chat event
		if (!MCoreConf.get().usingRecipientChatEvent) return;
		
		// Prepare vars
		MCorePlayerToRecipientChatEvent recipientEvent;
		final Player sender = event.getPlayer();
		String message = event.getMessage();
		String format = event.getFormat();
		
		// Pick the recipients to avoid the message getting sent without canceling the event.
		Set<Player> players = new HashSet<Player>(event.getRecipients());
		event.getRecipients().clear();
		
		// For each of the players
		for (Player recipient : players)
		{
			// Run the event for this unique recipient
			recipientEvent = new MCorePlayerToRecipientChatEvent(event.isAsynchronous(), sender, recipient, message, format);
			recipientEvent.run();
			
			// Format and send with the format and message from this recipient's own event. 
			String recipientMessage = String.format(recipientEvent.getFormat(), sender.getDisplayName(), recipientEvent.getMessage());
			recipient.sendMessage(recipientMessage);
		}
		
		// For the console
		recipientEvent = new MCorePlayerToRecipientChatEvent(event.isAsynchronous(), sender, Bukkit.getConsoleSender(), message, format);
		recipientEvent.run();
		event.setMessage(recipientEvent.getMessage());
		event.setFormat(recipientEvent.getFormat());
	}
	
	// -------------------------------------------- //
	// PERMISSION DENIED FORMAT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void permissionDeniedFormat(MCorePermissionDeniedFormatEvent event)
	{
		// If noone set a format already ...
		if (event.hasFormat()) return;
		
		// ... and we have a custom format in the config ...
		String customFormat = MCoreConf.get().getPermissionDeniedFormat(event.getPermissionName());
		if (customFormat == null) return;
		
		// ... then make use of that format.
		event.setFormat(customFormat);
	}
	
	// -------------------------------------------- //
	// CHAT TAB COMPLETE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void chatTabComplete(PlayerChatTabCompleteEvent event)
	{
		// So the player is watching ...
		Player watcher = event.getPlayer();
		
		// Get the lowercased token
		String tokenlc = event.getLastToken().toLowerCase();
		
		// Create a case insensitive set to check for already added stuff
		Set<String> current = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		current.addAll(event.getTabCompletions());
		
		// Add names of all online senders that match and isn't added yet. 
		for (String senderName : IdUtil.getOnlineNames())
		{
			if (!senderName.toLowerCase().startsWith(tokenlc)) continue;
			if (current.contains(senderName)) continue;
			if (!Mixin.canSee(watcher, senderName)) continue;
			
			event.getTabCompletions().add(senderName);
		}
	}
	
	// -------------------------------------------- //
	// VARIABLE BOOK
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void variableBook(PlayerCommandPreprocessEvent event)
	{
		event.setMessage(variableBook(event.getPlayer(), event.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void variableBook(AsyncPlayerChatEvent event)
	{
		event.setMessage(variableBook(event.getPlayer(), event.getMessage()));
	}
	
	public static String variableBook(Player player, String message)
	{
		// If we are using command variable book ...
		if (!MCoreConf.get().usingVariableBook) return message;
		
		// ... and the player has a book text ...
		String bookText = getBookText(player);
		if (bookText == null) return message;
		
		// ... and permission to use command variable book ...
		if (!MCorePerm.VARIABLEBOOK.has(player, false)) return message;
		
		// ... then replace.
		return StringUtils.replace(message, MCoreConf.get().variableBook, bookText);
	}
	
	public static String getBookText(CommandSender sender)
	{
		if (sender == null) return null;
		if (!(sender instanceof HumanEntity)) return null;
		HumanEntity human = (HumanEntity)sender;
		ItemStack item = human.getItemInHand();
		if (item == null) return null;
		if (!item.hasItemMeta()) return null;
		ItemMeta itemMeta = item.getItemMeta();
		if (!(itemMeta instanceof BookMeta)) return null;
		BookMeta bookMeta = (BookMeta)itemMeta;
		if (!bookMeta.hasPages()) return null;
		List<String> pages = bookMeta.getPages();
		String ret = Txt.implode(pages, " ");
		ret = ret.replaceAll("\\n+", " ");
		return ret;
	}
	
	// -------------------------------------------- //
	// EXPLOSION FX
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void explosionFx(EntityDamageByBlockEvent event)
	{
		// If an entity is taking damage from a block explosion ...
		DamageCause cause = event.getCause();
		if (cause != DamageCause.BLOCK_EXPLOSION) return;
		
		// ... and that explosion is a fake ...
		if (!SmokeUtil.fakeExplosion.booleanValue()) return;
		
		// ... then cancel the event and the damage.
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// PLAYER AND SENDER REFERENCES
	// -------------------------------------------- //
	// Note: For now we update both names and ids.
	// That way collections in plugins that haven't yet undergone update will still work.
	
	public static void setSenderReferences(CommandSender sender, CommandSender reference)
	{
		String id = IdUtil.getId(sender);
		if (id != null)
		{
			SenderColl.setSenderReferences(id, reference);
		}
		
		String name = IdUtil.getName(sender);
		if (name != null)
		{
			SenderColl.setSenderReferences(name, reference);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void setSenderReferencesLoginLowest(PlayerLoginEvent event)
	{
		setSenderReferences(event.getPlayer(), event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesLoginMonitor(PlayerLoginEvent event)
	{
		if (event.getResult() == Result.ALLOWED) return;
		
		setSenderReferences(event.getPlayer(), null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesQuitMonitor(PlayerQuitEvent event)
	{
		setSenderReferences(event.getPlayer(), null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesRegisterMonitor(MCoreSenderRegisterEvent event)
	{
		setSenderReferences(event.getSender(), event.getSender());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void setSenderReferencesUnregisterMonitor(MCoreSenderUnregisterEvent event)
	{
		setSenderReferences(event.getSender(), null);
	}
	
	// -------------------------------------------- //
	// AFTER EVENTS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerTeleportEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new MCoreAfterPlayerTeleportEvent(event), 0);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void after(PlayerRespawnEvent event)
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new MCoreAfterPlayerRespawnEvent(event, event.getPlayer().getLocation()), 0);
	}
	
	// -------------------------------------------- //
	// EVENT TOOL: causedByKick
	// -------------------------------------------- //
	
	public static Map<String,String> kickedPlayerReasons = new HashMap<String,String>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void causedByKick(PlayerKickEvent event)
	{
		final String name = event.getPlayer().getName();
		kickedPlayerReasons.put(name, event.getReason());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void causedByKick(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				kickedPlayerReasons.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// PLAYER LEAVE EVENT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void leaveEventKickCall(PlayerKickEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), true, "kick", event.getReason()).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitCall(PlayerQuitEvent event)
	{
		new MCorePlayerLeaveEvent(event.getPlayer(), false, "quit", null).run();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void leaveEventQuitClear(PlayerQuitEvent event)
	{
		// We do the schedule in order for the set to be correct through out the whole MONITOR priority state.
		final String name = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				MCorePlayerLeaveEvent.player2event.remove(name);
			}
		});
	}
	
	// -------------------------------------------- //
	// SYNC PLAYER ON LOGON AND LEAVE
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void syncOnPlayerLogin(PlayerLoginEvent event)
	{
		//MCore.get().log("LOWEST syncOnPlayerLogin", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void syncOnPlayerLeave(MCorePlayerLeaveEvent event)
	{
		//MCore.get().log("MONITOR syncOnPlayerLeave", event.getPlayer().getName());
		this.syncAllForPlayer(event.getPlayer());
	}
	
	public void syncAllForPlayer(Player player)
	{
		// TODO: For now we sync them both!
		String playerName = player.getName();
		String playerId = player.getUniqueId().toString();
		for (Coll<?> coll : Coll.getInstances())
		{
			if (!(coll instanceof SenderColl)) continue;
			SenderColl<?> pcoll = (SenderColl<?>)coll;
			pcoll.syncId(playerName);
			pcoll.syncId(playerId);
		}
	}
	
}
