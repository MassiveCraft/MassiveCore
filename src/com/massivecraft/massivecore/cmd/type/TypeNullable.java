package com.massivecraft.massivecore.cmd.type;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class TypeNullable<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Type<T> innerType;
	public Type<T> getInnerType() { return this.innerType; }
	
	protected Collection<String> nulls;
	public Collection<String> getNulls() { return this.nulls; }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static <T> TypeNullable<T> get(Type<T> inner, Collection<String> nulls)
	{
		return new TypeNullable<T>(inner, nulls);
	}
	
	public static <T> TypeNullable<T> get(Type<T> inner, String... nulls)
	{
		return new TypeNullable<T>(inner, nulls);
	}
	
	public static <T> TypeNullable<T> get(Type<T> inner)
	{
		return new TypeNullable<T>(inner);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeNullable(Type<T> inner, Collection<String> nulls)
	{
		if (inner == null) throw new NullPointerException("inner");
		if (nulls == null) nulls = Collections.emptySet();
		
		this.innerType = inner;
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
	public String getTypeName()
	{
		return this.getInnerType().getTypeName();
	}
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// Null?
		if (this.getNulls().contains(arg)) return null;
		
		// Inner
		return this.getInnerType().read(arg, sender);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Create Ret
		List<String> ret = new MassiveList<String>(this.getNulls());
		
		// Fill Ret
		ret.addAll(this.getInnerType().getTabList(sender, arg));
		
		// Return Ret
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerType().allowSpaceAfterTab();
	}

}
