package com.massivecraft.massivecore.command.type.convert;

import org.bukkit.inventory.ItemFlag;

import com.massivecraft.massivecore.command.type.enumeration.TypeItemFlag;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.item.ConverterFromItemFlag;
import com.massivecraft.massivecore.item.ConverterToItemFlag;

public class TypeConverterItemFlag extends TypeConverter<ItemFlag, String> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeConverterItemFlag i = new TypeConverterItemFlag();
	public static TypeConverterItemFlag get() { return i; }
	
	public TypeConverterItemFlag()
	{
		super(TypeItemFlag.get(), TypeString.get(), ConverterFromItemFlag.get(), ConverterToItemFlag.get());
	}

}
