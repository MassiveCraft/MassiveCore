package com.massivecraft.mcore.cmd;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.plugin.SimplePluginManager;

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
	
	// -------------------------------------------- //
	// SIMPLE PLUGIN MANAGER
	// -------------------------------------------- //
	
	public static CommandMap getSimplePluginManagerCommandMap(SimplePluginManager simplePluginManager)
	{
		return (CommandMap) get(SimplePluginManager.class, "commandMap", simplePluginManager);
	}
	
	// -------------------------------------------- //
	// COMMAND 
	// -------------------------------------------- //
	
	public static CommandMap getCommandDotCommandMap(Command command)
	{
		return (CommandMap) get(Command.class, "commandMap", command);
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
