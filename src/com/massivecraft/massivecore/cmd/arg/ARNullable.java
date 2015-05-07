package com.massivecraft.massivecore.cmd.arg;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class ARNullable<T> extends ARAbstract<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected AR<T> innerArgReader;
	public AR<T> getInnerArgReader() { return this.innerArgReader; }
	
	protected Collection<String> nulls;
	public Collection<String> getNulls() { return this.nulls; }
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static <T> ARNullable<T> get(AR<T> inner, Collection<String> nulls)
	{
		return new ARNullable<T>(inner, nulls);
	}
	
	public static <T> ARNullable<T> get(AR<T> inner, String... nulls)
	{
		return new ARNullable<T>(inner, nulls);
	}
	
	public static <T> ARNullable<T> get(AR<T> inner)
	{
		return new ARNullable<T>(inner);
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARNullable(AR<T> inner, Collection<String> nulls)
	{
		if (inner == null) throw new NullPointerException("inner");
		if (nulls == null) nulls = Collections.emptySet();
		
		this.innerArgReader = inner;
	}
	
	public ARNullable(AR<T> inner, String... nulls)
	{
		this(inner, Arrays.asList(nulls));
	}
	
	public ARNullable(AR<T> inner)
	{
		this(inner, MassiveCore.NOTHING_REMOVE);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getTypeName()
	{
		return this.getInnerArgReader().getTypeName();
	}
	
	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		// Null?
		if (this.getNulls().contains(arg)) return null;
		
		// Inner
		return this.getInnerArgReader().read(arg, sender);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// Create Ret
		List<String> ret = new MassiveList<String>(this.getNulls());
		
		// Fill Ret
		ret.addAll(this.getInnerArgReader().getTabList(sender, arg));
		
		// Return Ret
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerArgReader().allowSpaceAfterTab();
	}

}
