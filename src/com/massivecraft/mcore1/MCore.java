package com.massivecraft.mcore1;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;


import com.massivecraft.mcore1.cmd.Cmd;
import com.massivecraft.mcore1.lib.gson.GsonBuilder;
import com.massivecraft.mcore1.persist.One;
import com.massivecraft.mcore1.persist.Persist;
import com.massivecraft.mcore1.util.LibLoader;
import com.massivecraft.mcore1.util.PlayerUtil;
import com.massivecraft.mcore1.util.Txt;

public class MCore extends JavaPlugin
{
	MCoreServerListener serverListener;
	MCoreServerListenerMonitor serverListenerMonitor;
	MCorePlayerListener playerListener;
	
	// -------------------------------------------- //
	// PERSIST
	// -------------------------------------------- //
	private static Map<Object, Persist> persistInstances = new HashMap<Object, Persist>();
	public static Map<Object, Persist> getPersistInstances() { return persistInstances; }
	public static Persist getPersist(Object owner) { return persistInstances.get(owner); }
	public static void removePersist(Object owner) { persistInstances.remove(owner); }
	public static void createPersist(Object owner)
	{
		if (persistInstances.containsKey(owner)) return;
		persistInstances.put(owner, new Persist());
	}
	
	// -------------------------------------------- //
	// CMD
	// -------------------------------------------- //
	private static Map<Object, Cmd> cmdInstances = new HashMap<Object, Cmd>();
	public static Map<Object, Cmd> getCmdInstances() { return cmdInstances; }
	public static Cmd getCmd(Object owner) { return cmdInstances.get(owner); }
	public static void removeCmd(Object owner) { cmdInstances.remove(owner); }
	public static void createCmd(Object owner)
	{
		if (cmdInstances.containsKey(owner)) return;
		cmdInstances.put(owner, new Cmd());
	}
	public static boolean handleCommand(CommandSender sender, String commandString, boolean testOnly)
	{
		List<String> args = new ArrayList<String>(Arrays.asList(commandString.split("\\s+")));
		if (args.size() == 0) return false;
		String alias = args.get(0);
		args.remove(0);
		for (Cmd cmd : cmdInstances.values())
		{
			if (cmd.handleCommand(sender, alias, args, testOnly)) return true;
		}
		return false;
	}
	
	// -------------------------------------------- //
	// ONE
	// -------------------------------------------- //
	private static Map<MPlugin, One> oneInstances = new HashMap<MPlugin, One>();
	public static Map<MPlugin, One> getOneInstances() { return oneInstances; }
	public static One getOne(MPlugin owner) { return oneInstances.get(owner); }
	public static void removeOne(MPlugin owner) { oneInstances.remove(owner); }
	public static void createOne(MPlugin owner)
	{
		if (oneInstances.containsKey(owner)) return;
		oneInstances.put(owner, new One(owner));
	}
	
	// -------------------------------------------- //
	// LIBLOADER
	// -------------------------------------------- //
	private static Map<MPlugin, LibLoader> libLoaderInstances = new HashMap<MPlugin, LibLoader>();
	public static Map<MPlugin, LibLoader> getLibLoaderInstances() { return libLoaderInstances; }
	public static LibLoader getLibLoader(MPlugin owner) { return libLoaderInstances.get(owner); }
	public static void removeLibLoader(MPlugin owner) { libLoaderInstances.remove(owner); }
	public static void createLibLoader(MPlugin owner)
	{
		if (libLoaderInstances.containsKey(owner)) return;
		libLoaderInstances.put(owner, new LibLoader(owner));
	}
	
	// -------------------------------------------- //
	// DERP
	// -------------------------------------------- //
	
	public static Random random = new Random();
	
	public MCore()
	{	
		this.serverListener = new MCoreServerListener(this);
		this.serverListenerMonitor = new MCoreServerListenerMonitor(this);
		this.playerListener = new MCorePlayerListener(this);
	}
	
	@Override
	public void onDisable()
	{
		// Avoid memleak by clearing ???
		// Or will this trigger errors???
		//Persist.getRealms().clear();
	}

	@Override
	public void onEnable()
	{
		logPrefix = "["+this.getDescription().getName()+"] ";
		
		PlayerUtil.populateAllVisitorNames();
		
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		getPersistInstances().clear();
		
		// Register events
		Bukkit.getPluginManager().registerEvent(Type.PLAYER_PRELOGIN, this.playerListener, Priority.Lowest, this);
		Bukkit.getPluginManager().registerEvent(Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Lowest, this);
		Bukkit.getPluginManager().registerEvent(Type.SERVER_COMMAND, this.serverListener, Event.Priority.Lowest, this);
	}
	
	public static GsonBuilder getGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT);
	}
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	private static String logPrefix = null;
	public static void log(Object... msg)
	{
		log(Level.INFO, msg);
	}
	public static void log(Level level, Object... msg)
	{
		Logger.getLogger("Minecraft").log(level, logPrefix + Txt.implode(msg, " "));
	}
}
