package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassiveCorePerm;

public class ARAspect extends ARAbstractSelect<Aspect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARAspect i = new ARAspect();
	public static ARAspect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String typename()
	{
		return "aspect";
	}

	@Override
	public Aspect select(String arg, CommandSender sender)
	{
		return AspectColl.get().get(arg);
	}
	
	@Override
	public boolean canList(CommandSender sender)
	{
		return MassiveCorePerm.USYS_ASPECT_LIST.has(sender, false);
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return AspectColl.get().getIds();
	}
	
}
