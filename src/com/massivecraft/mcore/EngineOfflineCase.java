package com.massivecraft.mcore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

/**
 * In minecraft a player name is case insensitive but has one and only one correct casing when written out.
 * The correct casing is decided upon buying minecraft.
 * If you register "Steve" noone else can register "stEvE".
 * 
 * If you then try to log in using "STEVE" in the minecraft launcher the name will be corrected to "Steve".
 * This feature does however only work in online mode.
 * 
 * If you use a cracked client and an offline mode server you may log on to the server using both "Steve" and "STEVE".
 * This cause unintended side effects. Some file systems (windows) are case insensitive and some (unix) are case sensitive.
 * On Unix "Steve" and "STEVE" would have different inventories, locations etc. They would be considered different players.
 * That would be confusing for the players.
 * They would seem to lose their inventory when mistyping their name and this would only happen on some servers (those using unix). 
 * 
 * Additionally plugins may start acting weird if this rule is broken.
 * 
 * The purpose of this "engine" is to enforce the use of the one and same casing for offline mode servers.
 * You "register" the correct casing at the first login.
 * After that you get kicked when using an incorrect casing.
 * 
 */
public class EngineOfflineCase implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineOfflineCase i = new EngineOfflineCase();
	public static EngineOfflineCase get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		this.lowerToCorrect.clear();
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			this.registerCase(player.getName());
		}
		
		for (String pdname : MUtil.getPlayerDirectoryNames())
		{
			this.registerCase(pdname);
		}
		
		Bukkit.getPluginManager().registerEvents(this, MCore.get());
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private Map<String, String> lowerToCorrect = new HashMap<String, String>();
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private void registerCase(String playerName)
	{
		this.lowerToCorrect.put(playerName.toLowerCase(), playerName);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void forceOnePlayerNameCase(PlayerLoginEvent event)
	{
		// Stop if the feature is disabled
		if (!MCoreConf.get().isForcingOnePlayerNameCase()) return;
		
		// Stop if we are using online mode
		if (Bukkit.getOnlineMode()) return;
		
		// Prepare some variables
		final Player player = event.getPlayer();
		final String playerName = player.getName();
		final String playerNameLower = playerName.toLowerCase();
		
		// Kick if the player already is online
		// NOTE: Bukkit.getPlayerExact is case insensitive as it should be
		final Player onlinePlayer = Bukkit.getPlayerExact(playerName);
		if (onlinePlayer != null)
		{
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage(Txt.parse("<b>The player <h>%s <b>is already online.", onlinePlayer.getName()));
			return;
		}
		
		// Consider dat case!
		String correct = this.lowerToCorrect.get(playerNameLower);
		if (correct == null)
		{
			// "Register"
			this.registerCase(playerName);
		}
		else
		{
			// Kick if the case is wrong
			if (!correct.equals(playerName))
			{
				event.setResult(Result.KICK_OTHER);
				event.setKickMessage(Txt.parse("<b>Invalid uppercase and lowercase letters in name.\n<i>Please spell this name like \"<h>%s<i>\".", correct));
				return;
			}
		}
		
	}

}
