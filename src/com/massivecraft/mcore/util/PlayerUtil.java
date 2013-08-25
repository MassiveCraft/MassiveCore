package com.massivecraft.mcore.util;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.Packet8UpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.MCore;

public class PlayerUtil implements Listener
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected static Set<String> joinedPlayerNames = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// CONSTRUCTOR AND EVENT LISTENER
	// -------------------------------------------- //
	
	public PlayerUtil(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		joinedPlayerNames.clear();
		for (Player player : Bukkit.getOnlinePlayers())
		{
			joinedPlayerNames.add(player.getName());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void joinMonitor(PlayerJoinEvent event)
	{
		final String playerName = event.getPlayer().getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask(MCore.get(), new Runnable()
		{
			@Override
			public void run()
			{
				joinedPlayerNames.add(playerName);
			}
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void quitMonitor(PlayerQuitEvent event)
	{
		final String playerName = event.getPlayer().getName();
		joinedPlayerNames.remove(playerName);
	}
	
	// -------------------------------------------- //
	// PUBLIC METHODS
	// -------------------------------------------- //
	
	public static boolean isJoined(Player player)
	{
		if (player == null) throw new NullPointerException("player was null");
		return joinedPlayerNames.contains(player.getName());
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
