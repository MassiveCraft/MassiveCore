package com.massivecraft.massivecore.command.massivecore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.WebUtil;

public class CmdMassiveCoreCmdurl extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreCmdurl()
	{
		// Aliases
		this.addAliases("cmdurl");
		
		// Parameters
		this.addParameter(TypeString.get(), "url").setDesc("the url to load");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.CMDURL.node));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		String urlString = this.readArg();
		
		final URL url;
		try
		{
			url = new URL(urlString);
		}
		catch (MalformedURLException e)
		{
			msg("<b>Malformed URL: %s", e.getMessage());
			return;
		}
		
		// Apply 
		final Player commander = me;
		msg("<i>Loading <aqua>%s <i>...", urlString);
		async(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final List<String> lines = WebUtil.getLines(url);
					sync(new Runnable()
					{
						@Override
						public void run()
						{
							Mixin.msgOne(commander, "<i>... <h>%d <i>lines loaded. Now executing ...", lines.size());
							for (int i = 0; i <= lines.size() - 1; i++)
							{
								String line = lines.get(i);
								line = line.trim();
								if (line.length() == 0 || line.startsWith("#"))
								{
									Mixin.msgOne(commander, "<b>#%d: <i>%s", i, line);
									// Ignore the line
								}
								else
								{
									Mixin.msgOne(commander, "<g>#%d: <i>%s", i, line);
									// Run the line
									commander.chat(line);
								}
							}
						}
					});
					return;
				}
				catch (final Exception e)
				{
					sync(new Runnable()
					{
						@Override
						public void run()
						{
							Mixin.msgOne(commander, "<b>%s: %s", e.getClass().getSimpleName(), e.getMessage());
						}
					});
					return;
				}
			}
		});
	}
	
	// -------------------------------------------- //
	// ASYNC/SYNC SHORTHANDS
	// -------------------------------------------- //
	
	public static void sync(Runnable runnable)
	{
		Bukkit.getScheduler().runTask(MassiveCore.get(), runnable);
	}
	
	public static void async(Runnable runnable)
	{
		Bukkit.getScheduler().runTaskAsynchronously(MassiveCore.get(), runnable);
	}
	
}
