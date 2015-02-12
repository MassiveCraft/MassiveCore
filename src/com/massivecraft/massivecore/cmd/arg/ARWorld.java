package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARWorld extends ArgReaderAbstract<World>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARWorld i = new ARWorld();
	public static ARWorld get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public World read(String arg, CommandSender sender) throws MassiveException
	{
		World ret;
		
		String inner = ARWorldId.get().read(arg, sender);
		
		String worldId = inner;
		
		ret = Bukkit.getWorld(worldId);
		
		if (ret == null)
		{
			throw new MassiveException().addMsg("<b>The world \"<h>%s<b>\" could not be found.", arg);
		}
		
		return ret;
	}
	
}
