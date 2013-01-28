package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.usys.Aspect;
import com.massivecraft.mcore5.usys.AspectColl;

public class ARAspect extends ARAbstractSelect<Aspect>
{
	@Override
	public String typename()
	{
		return "aspect";
	}

	@Override
	public Aspect select(String str, CommandSender sender)
	{
		return AspectColl.i.get(str);
	}
	
	@Override
	public boolean canList(CommandSender sender)
	{
		return Permission.CMD_USYS_ASPECT_LIST.has(sender, false);
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return AspectColl.i.getIds();
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARAspect i = new ARAspect();
	public static ARAspect get() { return i; }
	
}
