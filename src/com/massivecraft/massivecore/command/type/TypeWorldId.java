package com.massivecraft.massivecore.command.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.mixin.Mixin;

public class TypeWorldId extends TypeAbstractChoice<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorldId i = new TypeWorldId();
	public static TypeWorldId get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "world";
	}

	@Override
	public String getVisualInner(String value, CommandSender sender)
	{
		return Mixin.getWorldDisplayName(value);
	}

	@Override
	public String getNameInner(String value)
	{
		return Mixin.getWorldAliasOrId(value);
	}

	@Override
	public String getIdInner(String value)
	{
		return value;
	}

	@Override
	public Collection<String> getAll()
	{
		return Mixin.getWorldIds();
	}
	
	@Override
	public boolean canSee(String value, CommandSender sender)
	{
		return Mixin.canSeeWorld(sender, value);
	}
	
}
