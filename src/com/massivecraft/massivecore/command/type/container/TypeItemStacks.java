package com.massivecraft.massivecore.command.type.container;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import com.massivecraft.massivecore.command.editor.CommandEditItemStacks;
import com.massivecraft.massivecore.command.editor.EditSettings;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.type.TypeItemStack;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

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
	public String getVisualInner(List<ItemStack> value, CommandSender sender)
	{
		// Empty
		if (value.isEmpty()) return EMPTY;
		
		// Integer Size
		return TypeInteger.get().getVisual(value.size(), sender);
		
		// TODO: Improve
	}
	
	@Override
	public <O> CommandEditAbstract<O, List<ItemStack>> createEditCommand(EditSettings<O> settings, Property<O, List<ItemStack>> property)
	{
		return new CommandEditItemStacks<O>(settings, property);
	}
	
}
