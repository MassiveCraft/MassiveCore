package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.mixin.MixinWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeWorld extends TypeAbstractChoice<World>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorld i = new TypeWorld();
	public static TypeWorld get() { return i; }
	public TypeWorld() { super(World.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getVisualInner(World value, CommandSender sender)
	{
		return MixinWorld.get().getWorldDisplayName(value.getName());
	}

	@Override
	public String getNameInner(World value)
	{
		return MixinWorld.get().getWorldAliasOrId(value.getName());
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
		return MixinWorld.get().canSeeWorld(sender, value.getName());
	}

}
