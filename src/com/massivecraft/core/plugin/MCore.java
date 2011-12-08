package com.massivecraft.core.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.core.persist.Persist;
import com.massivecraft.core.persist.PersistRealm;
import com.massivecraft.core.plugin.listener.PluginPlayerListener;
import com.massivecraft.core.plugin.listener.PluginServerListener;
import com.massivecraft.core.plugin.listener.PluginServerListenerMonitor;
import com.massivecraft.core.text.TextUtil;

public class MCore extends JavaPlugin
{
	PluginServerListener serverListener;
	PluginServerListenerMonitor serverListenerMonitor;
	PluginPlayerListener playerListener;
	
	public static MCore p;
	//public static PersistRealm persist;
	public static TextUtil text = new TextUtil();
	
	public MCore()
	{
		p = this;
		Persist.createRealm(this);
		//persist = Persist.getRealm(this);
		this.serverListener = new PluginServerListener(this);
		this.serverListenerMonitor = new PluginServerListenerMonitor(this);
		this.playerListener = new PluginPlayerListener(this);
	}
	
	public static PersistRealm getPersist()
	{
		return Persist.getRealm(p);
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
		// This is safe since all plugins using Persist should bukkit-depend this plugin.
		Persist.getRealms().clear();
		
		// Register events
		//Bukkit.getPluginManager().registerEvent(Type.PLUGIN_ENABLE, this.serverListener, Priority.Lowest, this); // Dangerous!?!?!
		//Bukkit.getPluginManager().registerEvent(Type.PLUGIN_DISABLE, this.serverListenerMonitor, Priority.Monitor, this); // Dangerous!?!?!
		Bukkit.getPluginManager().registerEvent(Type.PLAYER_JOIN, this.playerListener, Priority.Lowest, this);
	}

}
