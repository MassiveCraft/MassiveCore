package com.massivecraft.mcore1.util;

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
		// What is the name of the default world?
		String worldname = Bukkit.getWorlds().get(0).getName();
		
		// Find the player folder
		File playerfolder = new File(worldname, "players");
		
		// Populate by removing .dat
		for (File playerfile : playerfolder.listFiles())
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			allVisitorNames.add(playername);
		}
	}
}
