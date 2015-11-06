package com.massivecraft.massivecore.command.type;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;

public class TypeWorld extends TypeAbstractChoice<World>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorld i = new TypeWorld();
	public static TypeWorld get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(World value, CommandSender sender)
	{
		return Mixin.getWorldDisplayName(value.getName());
	}

	@Override
	public String getNameInner(World value)
	{
		return Mixin.getWorldAliasOrId(value.getName());
	}

	@Override
	public String getIdInner(World value)
	{
		return null;
	}

	@Override
	public Collection<World> getAll()
	{
		return Bukkit.getWorlds();
	}
	
	@Override
	public boolean canSee(World value, CommandSender sender)
	{
		return Mixin.canSeeWorld(sender, value.getName());
	}

}
