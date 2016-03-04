package com.massivecraft.massivecore.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class MassiveCommandVersion extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final String NOT_SPECIFIED = Txt.parse("<em><silver>not specified");
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final Plugin plugin;
	public Plugin getPlugin() { return this.plugin; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MassiveCommandVersion(Plugin plugin, String permissionName, String... aliases)
	{
		this(plugin, permissionName, Arrays.asList(aliases));
	}
	
	public MassiveCommandVersion(Plugin plugin, String permissionName, Collection<String> aliases)
	{
		this.plugin = plugin;
		
		if (permissionName != null)
		{
			this.addRequirements(RequirementHasPerm.get(permissionName));
		}
		this.setDesc("display plugin version");
		
		this.addAliases(aliases);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		PluginDescriptionFile pdf = this.getPlugin().getDescription();
		
		String name = pdf.getName();
		String version = pdf.getVersion();
		String website = pdf.getWebsite();
		
		String description = pdf.getDescription();
		if (description != null) description = Txt.parse("<i>"+description);
		
		String authors = null;
		List<String> authorList = pdf.getAuthors();
		if (authorList != null && authorList.size() > 0)
		{
			authors = Txt.implodeCommaAndDot(authorList, "<aqua>%s", "<i>, ", " <i>and ", "");
			authors = Txt.parse(authors);
		}
		
		this.sendTitle();
		this.sendEntry("name", name);
		this.sendEntry("version", version);
		this.sendEntry("website", website);
		this.sendEntry("authors", authors);
		this.sendEntry("description", description);
	}
	
	public void sendTitle()
	{
		message(Txt.titleize("Plugin Version & Information"));
	}
	
	public void sendEntry(String key, String value)
	{
		message(Txt.parse("<pink>%s: <aqua>%s", Txt.upperCaseFirst(key), value == null ? NOT_SPECIFIED : value));
	}

}
