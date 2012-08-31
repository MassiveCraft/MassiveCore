package com.massivecraft.mcore4.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;

import net.minecraft.server.DedicatedServer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet8UpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

public class PlayerUtil implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/**
	 * This is the latest created instance of this class.
	 */
	private static PlayerUtil i = null;
	
	/**
	 * We will use this folder later. 
	 */
	public static File playerfolder = getPlayerFolder();
	
	/**
	 * This map is populated using the player.dat files on disk.
	 * It is also populated when a player tries to log in to the server.
	 */
	protected static Map<String, String> nameToCorrectName = new ConcurrentSkipListMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * This map is used to improve the speed of name start lookups.
	 * Note that the keys in this map is lowercase.
	 */
	protected static Map<String, Set<String>> lowerCaseStartOfNameToCorrectNames = new ConcurrentSkipListMap<String, Set<String>>();
	
	// -------------------------------------------- //
	// CONSTRUCTOR AND EVENT LISTENER
	// -------------------------------------------- //
	
	public PlayerUtil(Plugin plugin)
	{
		if (i != null) return;
		i = this;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		populateCaseInsensitiveNameToCaseCorrectName();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLowestPlayerPreLoginEvent(PlayerPreLoginEvent event)
	{
		String newPlayerName = event.getName();
		String lowercaseNewPlayerName = newPlayerName.toLowerCase();
		
		// Add this name to the case-corrector map
		nameToCorrectName.put(newPlayerName, newPlayerName);
		
		// Update the cache
		for (Entry<String, Set<String>> entry : lowerCaseStartOfNameToCorrectNames.entrySet())
		{
			if (lowercaseNewPlayerName.startsWith(entry.getKey()))
			{
				entry.getValue().add(newPlayerName);
			}
		}
	}
	
	// -------------------------------------------- //
	// PUBLIC METHODS
	// -------------------------------------------- //
	
	/**
	 * This is a faster version of the getPlayerExact method since this one is exact for real (no to lower case stuff).
	 */
	@SuppressWarnings("unchecked")
	public static Player getPlayerExact(String exactPlayerName)
	{
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		List<EntityPlayer> entityPlayers = craftServer.getHandle().players;
		for (EntityPlayer entityPlayer : entityPlayers)
		{
			Player player = entityPlayer.netServerHandler.getPlayer();
			if (player.getName().equals(exactPlayerName))
			{
				return player;
			}
		}
		return null;
	}
	
	/**
	 * This method simply checks if the playerName is a valid one.
	 * Mojangs rules for Minecraft character registration is used.
	 */
	public static boolean isValidPlayerName(final String playerName)
	{
		return Pattern.matches("^[a-zA-Z0-9_]{2,16}$", playerName);
	}
	
	public static Set<String> getAllVisitorNames()
	{
		return nameToCorrectName.keySet();
	}
	
	/**
	 * This method takes a player name and returns the same name but with correct case.
	 * Null is returned if the correct case can not be determined.
	 */
	public static String fixPlayerNameCase(final String playerName)
	{
		return nameToCorrectName.get(playerName);
	}
	
	/**
	 * Find all player names starting with a certain string (not case sensitive).
	 * This method will return the names of offline players as well as online players.
	 */
	public static Set<String> getAllPlayerNamesCaseinsensitivelyStartingWith(final String startOfName)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		String lowercaseStartOfName = startOfName.toLowerCase();
		
		// Try to fetch from the cache
		Set<String> cachedNames = lowerCaseStartOfNameToCorrectNames.get(lowercaseStartOfName);
		if (cachedNames != null)
		{
			ret.addAll(cachedNames);
			return ret;
		}
		
		// Build it the hard way if cache did not exist
		
		ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (String correctName : nameToCorrectName.values())
		{
			if (correctName.toLowerCase().startsWith(lowercaseStartOfName))
			{
				ret.add(correctName);
			}
		}
		
		// Add it to the cache
		Set<String> shallowCopyForCache = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		shallowCopyForCache.addAll(ret);
		lowerCaseStartOfNameToCorrectNames.put(lowercaseStartOfName, shallowCopyForCache);
		
		return ret;
	}
	
	/**
	 * In Minecraft a playername can be 16 characters long. One sign line is however only 15 characters long.
	 * If we find a 15 character long playername on a sign it could thus refer to more than one player.
	 * This method finds all possible matching player names.
	 */
	public static Set<String> interpretPlayerNameFromSign(String playerNameFromSign)
	{
		Set<String> ret = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		if (playerNameFromSign.length() > 15)
		{
			// This case will in reality not happen.
			ret.add(playerNameFromSign);
			return ret;
		}
		
		if (playerNameFromSign.length() == 15)
		{
			ret.addAll(PlayerUtil.getAllPlayerNamesCaseinsensitivelyStartingWith(playerNameFromSign));
		}
		else
		{
			String fixedPlayerName = PlayerUtil.fixPlayerNameCase(playerNameFromSign);
			if (fixedPlayerName != null)
			{
				ret.add(fixedPlayerName);
			}
		}
		
		return ret;
	}
	
	/**
	 * It seems the OfflinePlayer#getLastPlayed in Bukkit is broken.
	 * It occasionally returns invalid values. Therefore we use this instead.
	 * The playerName must be the full name but is not case sensitive. 
	 */
	public static long getLastPlayed(String playerName)
	{
		String playerNameCC = fixPlayerNameCase(playerName);
		if (playerNameCC == null) return 0;
			
		Player player = Bukkit.getPlayerExact(playerNameCC);
		if (player != null && player.isOnline()) return System.currentTimeMillis();
		
		File playerFile = new File(playerfolder, playerNameCC+".dat");
		return playerFile.lastModified();
	}
	
	/**
	 * Updates the players food and health information.
	 */
	public static void sendHealthFoodUpdatePacket(Player player)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.netServerHandler.sendPacket(new Packet8UpdateHealth(eplayer.getHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
	}
	
	// -------------------------------------------- //
	// INTERNAL METHODS
	// -------------------------------------------- //
	
	protected static void populateCaseInsensitiveNameToCaseCorrectName()
	{   
		// Populate by removing .dat
		for (File playerfile : playerfolder.listFiles())
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			nameToCorrectName.put(playername, playername);
		}
	}
	
	/**
	 * You might ask yourself why we do this in such a low-level way.
	 * The reason is this info is not yet "compiled" for plugins that init early.
	 */
	protected static File getPlayerFolder()
	{
		CraftServer cserver = (CraftServer)Bukkit.getServer();
		DedicatedServer dserver = (DedicatedServer)cserver.getServer();
		String levelName = dserver.propertyManager.getString("level-name", "world");
		return new File(Bukkit.getWorldContainer(), new File(levelName, "players").getPath());
	}
	
}
