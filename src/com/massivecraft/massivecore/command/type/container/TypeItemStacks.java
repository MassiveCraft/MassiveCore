package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditItemStacks;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.TypeItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TypeItemStacks extends TypeList<ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemStacks i = new TypeItemStacks();
	public static TypeItemStacks get() { return i; }
	public TypeItemStacks()
	{
		super(TypeItemStack.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public <O> CommandEditAbstract<O, List<ItemStack>> createEditCommand(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		return new CommandEditItemStacks<>(settings, property);
	}
	
}
