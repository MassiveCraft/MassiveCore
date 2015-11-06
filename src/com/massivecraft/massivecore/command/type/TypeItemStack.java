package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.massivecore.MassiveException;

public class TypeItemStack extends TypeAbstract<ItemStack>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeItemStack i = new TypeItemStack();
	public static TypeItemStack get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(ItemStack value, CommandSender sender)
	{
		// TODO: Implement
		return null;
	}

	@Override
	public String getNameInner(ItemStack value)
	{
		// TODO: Implement
		return null;
	}

	@Override
	public String getIdInner(ItemStack value)
	{
		// TODO: Implement
		return null;
	}

	@Override
	public ItemStack read(String arg, CommandSender sender) throws MassiveException
	{
		// TODO: Implement
		throw new MassiveException().addMsg("<b>Not implemented!");
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// TODO: Implement
		return Collections.emptyList();
	}

}
