package com.massivecraft.massivecore.command.type.primitive;

import com.massivecraft.massivecore.command.type.TypeAbstract;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;

// This type is pretty weak and dysfunctional.
// It's intended to be used as a place holder.
// You can pass it instead of null for the sake of NPE evasion.
// It was initially created for usage within TypeTransformer.
public class TypeObject<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	/*private static TypeObject<Object> i = new TypeObject<Object>();
	
	@SuppressWarnings("unchecked")
	public static <T> TypeObject<T> get() { return (TypeObject<T>) i; }*/
	
	public static <T> TypeObject<T> get(Class<T> clazz) { return new TypeObject<>(clazz); }
	
	public TypeObject(Class<T> clazz)
	{
		super(clazz);
		this.setInnerProperties(clazz);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getName()
	{
		return "object";
	}
	
	@Override
	public String getIdInner(T value)
	{
		return value.toString();
	}
	
	@Override
	public T read(String arg, CommandSender sender)
	{
		return null;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptySet();
	}

}
