package com.massivecraft.mcore;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.MCoreBukkitCommand;

public class EngineCommandRegistration extends EngineAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineCommandRegistration i = new EngineCommandRegistration();
	public static EngineCommandRegistration get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return MCore.get();
	}
	
	@Override
	public Long getPeriod()
	{
		return 20L;
	}
	
	// -------------------------------------------- //
	// TASK
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		updateRegistrations();
	}
	
	// -------------------------------------------- //
	// UPDATE REGISTRATIONS
	// -------------------------------------------- //
	
	public static void updateRegistrations()
	{
		// Get the SimpleCommandMap and it's knownCommands.
		SimpleCommandMap simpleCommandMap = getSimpleCommandMap();
		Map<String, Command> knownCommands = getSimpleCommandMapDotKnownCommands(simpleCommandMap);
		
		// For each known command ...
		Iterator<Entry<String, Command>> iter = knownCommands.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, Command> entry = iter.next();
			Command command = entry.getValue();
			
			// ... if this command is a MCoreBukkitCommand ...
			if (!(command instanceof MCoreBukkitCommand)) continue;
			
			// ... unregister it.
			command.unregister(simpleCommandMap);
			iter.remove();
		}
		
		// For each MCommand that is supposed to be registered ...
		for (MCommand mcommand : MCommand.getRegisteredCommands())
		{
			// ... and for each of it's aliases ...
			for (String alias : mcommand.getAliases())
			{
				// ... clean the alias ...
				alias = alias.trim().toLowerCase();
				
				// ... unregister current occupant of that alias ...
				Command previousOccupant = knownCommands.remove(alias);
				if (previousOccupant != null)
				{
					previousOccupant.unregister(simpleCommandMap);
				}
				
				// ... create a new MCoreBukkitCommand ...
				MCoreBukkitCommand command = new MCoreBukkitCommand(alias, mcommand);
				
				// ... and finally register it.
				simpleCommandMap.register("MCore", command);
			}
		}
	}
	
	// -------------------------------------------- //
	// GETTERS
	// -------------------------------------------- //
	
	public static SimpleCommandMap getSimpleCommandMap()
	{
		Server server = Bukkit.getServer();
		return (SimpleCommandMap) get(server.getClass(), "commandMap", server);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Command> getSimpleCommandMapDotKnownCommands(SimpleCommandMap simpleCommandMap)
	{
		return (Map<String, Command>) get(SimpleCommandMap.class, "knownCommands", simpleCommandMap);
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
