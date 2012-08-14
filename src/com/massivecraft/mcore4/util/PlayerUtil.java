package com.massivecraft.mcore4.util;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.server.DedicatedServer;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet8UpdateHealth;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil
{
	private static Set<String> allVisitorNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getAllVisitorNames() { return allVisitorNames; }
	public static void populateAllVisitorNames()
	{
		// Find the player folder
	    CraftServer cserver = (CraftServer)Bukkit.getServer();
	    DedicatedServer dserver = (DedicatedServer)cserver.getServer();
		String levelName = dserver.propertyManager.getString("level-name", "world");
		File playerfolder = new File(Bukkit.getWorldContainer(), new File(levelName, "players").getPath());
		
		// Populate by removing .dat
		for (File playerfile : playerfolder.listFiles())
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			allVisitorNames.add(playername);
		}
	}
	
	public static void sendHealthFoodUpdatePacket(Player player)
	{
		CraftPlayer cplayer = (CraftPlayer)player;
		EntityPlayer eplayer = cplayer.getHandle();
		eplayer.netServerHandler.sendPacket(new Packet8UpdateHealth(eplayer.getHealth(), eplayer.getFoodData().a(), eplayer.getFoodData().e()));
	}
	
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
}
