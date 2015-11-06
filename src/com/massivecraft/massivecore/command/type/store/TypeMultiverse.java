package com.massivecraft.massivecore.command.type.store;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.MultiverseColl;

public class TypeMultiverse extends TypeEntity<Multiverse>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMultiverse i = new TypeMultiverse();
	public static TypeMultiverse get() { return i; }
	public TypeMultiverse()
	{
		super(MultiverseColl.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean canList(CommandSender sender)
	{
		return MassiveCorePerm.USYS_MULTIVERSE_LIST.has(sender, false);
	}

}
