package com.massivecraft.mcore5.util;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.Packet8UpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class PlayerUtil implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	/**
	 * We will use this folder later.
	 */
	public static File playerDirectory = MUtil.getPlayerDirectory();
	
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
		Bukkit.getPluginManager().registerEvents(this, plugin);

		for (String playername : MUtil.getPlayerDirectoryNames())
		{
			nameToCorrectName.put(playername, playername);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLowestPlayerLoginEvent(PlayerLoginEvent event)
	{
		String newPlayerName = event.getPlayer().getName();
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
		
		File playerFile = new File(playerDirectory, playerNameCC+".dat");
		return playerFile.lastModified();
	}
	
	/**
	 * Updates the players food and health information.
	 */
	public static void sendHealthFoodUpdatePacket(Player player)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.playerConnection.sendPacket(new Packet8UpdateHealth(eplayer.getHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
	}
	
}
