package com.massivecraft.mcore2dev;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.mcore2dev.cmd.Cmd;
import com.massivecraft.mcore2dev.lib.gson.GsonBuilder;
import com.massivecraft.mcore2dev.persist.One;
import com.massivecraft.mcore2dev.persist.Persist;
import com.massivecraft.mcore2dev.util.LibLoader;
import com.massivecraft.mcore2dev.util.PlayerUtil;
import com.massivecraft.mcore2dev.util.Txt;

public class MCore extends JavaPlugin
{
	InternalListener listener;
	
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
		this.listener = new InternalListener(this);
	}
	
	public static GsonBuilder getGsonBuilder()
	{
		return new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT);
	}
	
	// -------------------------------------------- //
	// SPOUT INTEGRATION
	// -------------------------------------------- //
	/*protected boolean spoutIsIntegrated = false;
	protected void integrateSpout()
	{
		if (spoutIsIntegrated) return;
		if ( ! Bukkit.getPluginManager().isPluginEnabled("Spout")) return;
		
		// Ok we should be safe :) Lets integrate! 
		this.spoutIsIntegrated = true;
		
		
	}*/
	
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
