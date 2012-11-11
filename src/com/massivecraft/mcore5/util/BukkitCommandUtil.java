package com.massivecraft.mcore5.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;

public class BukkitCommandUtil
{
	public static SimpleCommandMap getBukkitCommandMap()
	{
		CraftServer craftServer = (CraftServer)Bukkit.getServer();
		return craftServer.getCommandMap();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Command> getKnownCommandsFromSimpleCommandMap(SimpleCommandMap scm)
	{
		try
		{
			Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
			field.setAccessible(true);
			return (Map<String, Command>) field.get(scm);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
