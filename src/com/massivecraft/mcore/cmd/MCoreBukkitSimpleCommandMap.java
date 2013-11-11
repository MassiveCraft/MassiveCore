package com.massivecraft.mcore.cmd;

import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;


public class MCoreBukkitSimpleCommandMap extends SimpleCommandMap
{
	// -------------------------------------------- //
	// INJECT
	// -------------------------------------------- //
	
	public static MCoreBukkitSimpleCommandMap get()
	{
		SimpleCommandMap ret = BukkitCommandDoor.getSimpleCommandMap();
		if (!(ret instanceof MCoreBukkitSimpleCommandMap)) return null;
		return (MCoreBukkitSimpleCommandMap) ret;
	}
	
	public static boolean isInjected()
	{
		return get() != null;
	}
	
	public static void inject()
	{
		if (isInjected()) return;
		MCoreBukkitSimpleCommandMap instance = new MCoreBukkitSimpleCommandMap();
		BukkitCommandDoor.setSimpleCommandMap(instance);
		BukkitCommandDoor.setSimplePluginManagerCommandMap(BukkitCommandDoor.getSimplePluginManager(), instance);
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final LinkedHashSet<MCoreBukkitCommand> mcoreBukkitCommands = new LinkedHashSet<MCoreBukkitCommand>();
	public LinkedHashSet<MCoreBukkitCommand> getMCoreBukkitCommands() { return this.mcoreBukkitCommands; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCoreBukkitSimpleCommandMap(SimpleCommandMap simpleCommandMap)
	{
		// Trigger the super constructor
		super(Bukkit.getServer());
		
		// Fetch non static collection content 
		this.knownCommands.putAll(BukkitCommandDoor.getSimpleCommandMapDotKnownCommands(simpleCommandMap));
		this.aliases.addAll(BukkitCommandDoor.getSimpleCommandMapDotAliases(simpleCommandMap));
		
		// Convert registrations
		for (Entry<String, Command> entry : this.knownCommands.entrySet())
		{
			Command command = entry.getValue();
			if (BukkitCommandDoor.getCommandDotCommandMap(command) == null) continue;
			BukkitCommandDoor.setCommandDotCommandMap(command, this);
		}
	}
	
	public MCoreBukkitSimpleCommandMap()
	{
		this(BukkitCommandDoor.getSimpleCommandMap());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean register(String label, String fallbackPrefix, Command command)
	{
		// Bukkit
		if (!(command instanceof MCoreBukkitCommand))
		{
			return super.register(label, fallbackPrefix, command);
		}
		
		// MCore
		command.register(this);
		this.getMCoreBukkitCommands().add((MCoreBukkitCommand)command);
		
		return true;
	}
	
	@Override
	public Command getCommand(String name)
	{
		// MCore
		for (MCoreBukkitCommand mbc : this.getMCoreBukkitCommands())
		{
			for (String alias : mbc.getAliases())
			{
				if (alias.equalsIgnoreCase(name))
				{
					return mbc;
				}
			}
		}
		
		// Bukkit
		return super.getCommand(name);
	}

}
