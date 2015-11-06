package com.massivecraft.massivecore.command.type.primitive;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.type.TypeAbstractSimple;

public abstract class TypeAbstractNumber<T extends Number> extends TypeAbstractSimple<T>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public static final List<String> TAB_LIST = Collections.singletonList("1");
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	public TypeAbstractNumber()
	{
		this.setVisualColor(COLOR_NUMBER);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return TAB_LIST;
	}
	
}
