package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.MultiverseColl;

public class TypeMultiverse extends TypeAbstractSelect<Multiverse>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMultiverse i = new TypeMultiverse();
	public static TypeMultiverse get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Multiverse select(String arg, CommandSender sender)
	{
		return MultiverseColl.get().get(arg);
	}
	
	@Override
	public boolean canList(CommandSender sender)
	{
		return MassiveCorePerm.USYS_MULTIVERSE_LIST.has(sender, false);
	}

	@Override
	public Collection<String> altNames(CommandSender sender)
	{
		return MultiverseColl.get().getIds();
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return this.altNames(sender);
	}

}
