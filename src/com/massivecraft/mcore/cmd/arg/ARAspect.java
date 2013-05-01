package com.massivecraft.mcore.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.AspectColl;
import com.massivecraft.mcore.MCorePerm;

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
		return MCorePerm.CMD_MCORE_USYS_ASPECT_LIST.has(sender, false);
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return AspectColl.get().getIds();
	}
	
}
