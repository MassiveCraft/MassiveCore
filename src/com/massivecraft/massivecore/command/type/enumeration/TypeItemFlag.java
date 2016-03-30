package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.inventory.ItemFlag;

public class TypeItemFlag extends TypeEnum<ItemFlag>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemFlag i = new TypeItemFlag();
	public static TypeItemFlag get() { return i; }
	public TypeItemFlag()
	{
		super(ItemFlag.class);
	}

}
