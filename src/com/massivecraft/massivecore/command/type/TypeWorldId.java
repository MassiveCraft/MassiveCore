package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.mixin.MixinWorld;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class TypeWorldId extends TypeAbstractChoice<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeWorldId i = new TypeWorldId();
	public static TypeWorldId get() { return i; }
	public TypeWorldId() { super(String.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "world";
	}

	@Override
	public String getVisualInner(String value, CommandSender sender)
	{
		return MixinWorld.get().getWorldDisplayName(value);
	}

	@Override
	public String getNameInner(String value)
	{
		return MixinWorld.get().getWorldAliasOrId(value);
	}

	@Override
	public String getIdInner(String value)
	{
		return value;
	}

	@Override
	public Collection<String> getAll()
	{
		return MixinWorld.get().getWorldIds();
	}
	
	@Override
	public boolean canSee(String value, CommandSender sender)
	{
		return MixinWorld.get().canSeeWorld(sender, value);
	}
	
}
