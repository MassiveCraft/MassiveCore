package com.massivecraft.mcore5.mixin;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.server.v1_4_R1.DedicatedServer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.util.MUtil;

public class DefaultSenderIdFixerMixin implements SenderIdFixerMixin, Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DefaultSenderIdFixerMixin i = new DefaultSenderIdFixerMixin();
	public static DefaultSenderIdFixerMixin get() { return i; }
	
	// -------------------------------------------- //
	// REGISTER & UNREGISTER
	// -------------------------------------------- //
	
	public void register()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, MCore.p);
	}
	
	public void unregister()
	{
		HandlerList.unregisterAll(this);
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		this.populateIdToCorrectId();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected File playerfolder = this.getPlayerFolder();
	protected Map<String, String> idToCorrectId = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean canFix(String name)
	{
		return this.reqFix(name) != null;
	}
	
	@Override
	public String tryFix(String name)
	{
		String ret = this.reqFix(name);
		if (ret != null) return ret;
		return name;
	}

	@Override
	public String reqFix(String name)
	{
		if (!MUtil.isValidPlayerName(name)) return name;
		return this.idToCorrectId.get(name);
	}
	
	// -------------------------------------------- //
	// LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		String id = event.getPlayer().getName();
		idToCorrectId.put(id, id);
	}
	
	// -------------------------------------------- //
	// INTERNAL METHODS
	// -------------------------------------------- //
	
	/**
	 * You might ask yourself why we do this in such a low-level way.
	 * The reason is this info is not yet "compiled" for plugins that init early.
	 */
	protected File getPlayerFolder()
	{
		CraftServer cserver = (CraftServer)Bukkit.getServer();
		DedicatedServer dserver = (DedicatedServer)cserver.getServer();
		String levelName = dserver.propertyManager.getString("level-name", "world");
		return new File(Bukkit.getWorldContainer(), new File(levelName, "players").getPath());
	}
	
	protected void populateIdToCorrectId()
	{   
		// List the files in the player folder
		File[] playerfiles = this.playerfolder.listFiles();
		
		// The player file may not exist yet
		if (playerfiles == null) return;
		
		// Populate by removing .dat
		for (File playerfile : playerfiles)
		{
			String filename = playerfile.getName();
			String playername = filename.substring(0, filename.length()-4);
			this.idToCorrectId.put(playername, playername);
		}
	}

}