package com.massivecraft.massivecore.cmd.massivecore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
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
		
		// Args
		this.addRequiredArg("url");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.CMDURL.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		String urlString = this.arg(0);
		
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
					final List<String> lines = WebUtil.getUrlLines(url, 5000);
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
