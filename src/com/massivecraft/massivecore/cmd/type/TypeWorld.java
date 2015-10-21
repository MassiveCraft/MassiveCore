package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class TypeWorld extends TypeAbstract<World>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorld i = new TypeWorld();
	public static TypeWorld get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public World read(String arg, CommandSender sender) throws MassiveException
	{
		String worldId = TypeWorldId.get().read(arg, sender);
		
		World ret = Bukkit.getWorld(worldId);
		
		if (ret == null)
		{
			throw new MassiveException().addMsg("<b>The world \"<h>%s<b>\" could not be found.", arg);
		}
		
		return ret;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return TypeWorldId.get().getTabList(sender, arg);
	}

}
