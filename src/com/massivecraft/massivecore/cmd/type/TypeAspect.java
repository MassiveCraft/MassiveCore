package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassiveCorePerm;

public class TypeAspect extends TypeAbstractSelect<Aspect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeAspect i = new TypeAspect();
	public static TypeAspect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

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
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}
	
}
