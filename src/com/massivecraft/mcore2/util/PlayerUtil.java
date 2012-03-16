package com.massivecraft.mcore2.util;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;

public class PlayerUtil
{
	private static Set<String> allVisitorNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	public static Set<String> getAllVisitorNames() { return allVisitorNames; }
	public static void populateAllVisitorNames()
	{
		// Find the player folder
		File playerfolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "players");
		
		// Populate by removing .dat
		for (File playerfile : playerfolder.listFiles())
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			allVisitorNames.add(playername);
		}
	}
}
