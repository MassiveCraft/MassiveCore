package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class TypeNullable<T> extends TypeWrapper<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Collection<String> nulls;
	public Collection<String> getNulls() { return this.nulls; }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static <T> TypeNullable<T> get(Type<T> inner, Collection<String> nulls)
	{
		return new TypeNullable<>(inner, nulls);
	}
	
	public static <T> TypeNullable<T> get(Type<T> inner, String... nulls)
	{
		return new TypeNullable<>(inner, nulls);
	}
	
	public static <T> TypeNullable<T> get(Type<T> inner)
	{
		return new TypeNullable<>(inner);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeNullable(Type<T> inner, Collection<String> nulls)
	{
		super(inner);
		if (nulls == null) nulls = Collections.emptySet();
		this.nulls = nulls;
	}
	
	public TypeNullable(Type<T> inner, String... nulls)
	{
		this(inner, Arrays.asList(nulls));
	}
	
	public TypeNullable(Type<T> inner)
	{
		this(inner, MassiveCore.NOTHING_REMOVE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// Null?
		if (this.getNulls().contains(arg)) return null;
		
		// Super
		return super.read(arg, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Super
		Collection<String> ret = super.getTabList(sender, arg);

		// Add nulls
		ret.addAll(this.getNulls());

		// Return
		return ret;
	}

}
