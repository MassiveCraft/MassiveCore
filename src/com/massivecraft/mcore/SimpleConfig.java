package com.massivecraft.mcore;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.massivecraft.mcore.store.accessor.Accessor;
import com.massivecraft.mcore.util.DiscUtil;
import com.massivecraft.mcore.xlib.gson.Gson;

public class SimpleConfig
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	protected transient Plugin plugin;
	protected Plugin getPlugin() { return this.plugin; }
	
	protected transient File file;
	protected File getFile() { return this.file; }
	
	public SimpleConfig(Plugin plugin, File file)
	{
		this.plugin = plugin;
		this.file = file;
	}
	
	public SimpleConfig(Plugin plugin, String confname)
	{
		this(plugin, new File(plugin.getDataFolder(), confname+".json"));
	}
	
	public SimpleConfig(Plugin plugin)
	{
		this(plugin, "conf");
	}
	
	// -------------------------------------------- //
	// IO
	// -------------------------------------------- //
	
	private Gson getGson()
	{
		if (this.plugin instanceof MPlugin)
		{
			return ((MPlugin)this.plugin).gson;
		}
		return MCore.gson;
	}
	
	protected static boolean contentRequestsDefaults(String content)
	{
		if (content == null) return false;
		char c = content.charAt(0);
		return c == 'd' || c == 'D';
	}
	
	public void load()
	{
		if (this.getFile().isFile())
		{
			String content = DiscUtil.readCatch(this.getFile());
			Object toShallowLoad = null;
			if (contentRequestsDefaults(content))
			{
				try
				{
					toShallowLoad = this.getClass().newInstance();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}
			}
			else
			{
				toShallowLoad = this.getGson().fromJson(content, this.getClass());
			}
			Accessor.get(this.getClass()).copy(toShallowLoad, this);
		}
		save();
	}
	
	public void save()
	{
		String content = DiscUtil.readCatch(this.getFile());
		if (contentRequestsDefaults(content)) return;
		content = this.getGson().toJson(this);
		DiscUtil.writeCatch(file, content);
	}
}

