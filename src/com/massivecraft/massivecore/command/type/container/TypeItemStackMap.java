package com.massivecraft.massivecore.command.type.container;

import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditItemStackMap;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.TypeItemStack;
import com.massivecraft.massivecore.command.type.combined.TypeEntry;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

public class TypeItemStackMap extends TypeMap<Integer, ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemStackMap i = new TypeItemStackMap();
	public static TypeItemStackMap get() { return i; }
	public TypeItemStackMap()
	{
		super(TypeEntry.get(TypeInteger.get(), TypeItemStack.get()));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public <O> CommandEditAbstract<O, Map<Integer, ItemStack>> createEditCommand(EditSettings<O> settings, Property<O, Map<Integer, ItemStack>> property)
	{
		return new CommandEditItemStackMap<O>(settings, property);
	}
	
}
