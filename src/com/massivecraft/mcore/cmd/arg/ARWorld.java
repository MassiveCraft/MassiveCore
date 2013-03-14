package com.massivecraft.mcore.cmd.arg;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

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
	public ArgResult<World> read(String arg, CommandSender sender)
	{
		ArgResult<World> ret = new ArgResult<World>();
		
		ArgResult<String> inner = ARWorldId.get().read(arg, sender);
		if (inner.hasErrors())
		{
			ret.setErrors(inner.getErrors());
			return ret;
		}
		
		String worldId = inner.getResult();
		
		World world = Bukkit.getWorld(worldId);
		if (world == null)
		{
			ret.setErrors("<b>The world could not be found.");
		}
		else
		{
			ret.setResult(world);
		}
		
		return ret;
	}
	
}
