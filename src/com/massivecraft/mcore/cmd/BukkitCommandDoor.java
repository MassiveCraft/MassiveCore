package com.massivecraft.mcore.cmd;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.plugin.SimplePluginManager;

@SuppressWarnings("unchecked")
public class BukkitCommandDoor
{
	// -------------------------------------------- //
	// SINGLETON
	// -------------------------------------------- //
	
	public static CraftServer getCraftServer()
	{
		return (CraftServer)Bukkit.getServer();
	}
	
	public static SimpleCommandMap getSimpleCommandMap()
	{
		return getCraftServer().getCommandMap();
	}
	
	public static void setSimpleCommandMap(SimpleCommandMap simpleCommandMap)
	{
		set(CraftServer.class, "commandMap", getCraftServer(), simpleCommandMap);
	}
	
	public static SimplePluginManager getSimplePluginManager()
	{
		return (SimplePluginManager)Bukkit.getPluginManager();
	}
	
	// -------------------------------------------- //
	// SIMPLE COMMAND MAP
	// -------------------------------------------- //
	
	public static Map<String, Command> getSimpleCommandMapDotKnownCommands(SimpleCommandMap simpleCommandMap)
	{
		return (Map<String, Command>) get(SimpleCommandMap.class, "knownCommands", simpleCommandMap);
	}
	
	public static Set<String> getSimpleCommandMapDotAliases(SimpleCommandMap simpleCommandMap)
	{
		return (Set<String>) get(SimpleCommandMap.class, "aliases", simpleCommandMap);
	}
	
	// -------------------------------------------- //
	// SIMPLE PLUGIN MANAGER
	// -------------------------------------------- //
	
	public static CommandMap getSimplePluginManagerCommandMap(SimplePluginManager simplePluginManager)
	{
		return (CommandMap) get(SimplePluginManager.class, "commandMap", simplePluginManager);
	}
	
	public static void setSimplePluginManagerCommandMap(SimplePluginManager simplePluginManager, CommandMap commandMap)
	{
		set(SimplePluginManager.class, "commandMap", simplePluginManager, commandMap);
	}
	
	// -------------------------------------------- //
	// COMMAND 
	// -------------------------------------------- //
	
	public static CommandMap getCommandDotCommandMap(Command command)
	{
		return (CommandMap) get(Command.class, "commandMap", command);
	}
	
	public static void setCommandDotCommandMap(Command command, CommandMap commandMap)
	{
		set(Command.class, "commandMap", command, commandMap);
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static Object get(Class<?> clazz, String fieldName, Object object)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	public static void set(Class<?> clazz, String fieldName, Object object, Object value)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		}
		catch (Exception e)
		{
			return;
		}
	}

}
