package com.massivecraft.massivecore.command.type;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class TypeWrapper<T> extends TypeAbstract<T>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TypeWrapper(Type<T> inner)
	{
		if (inner == null) throw new NullPointerException("inner");
		this.setInnerType(inner);
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
	public String getVisualInner(T value, CommandSender sender)
	{
		return this.getInnerType().getVisualInner(value, sender);
	}

	@Override
	public String getNameInner(T value)
	{
		return this.getInnerType().getNameInner(value);
	}

	@Override
	public String getIdInner(T value)
	{
		return this.getInnerType().getIdInner(value);
	}

	@Override
	public T read(String arg, CommandSender sender) throws MassiveException
	{
		Type<T> inner = this.getInnerType();
		return inner.read(arg, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return new MassiveList<String>(this.getInnerType().getTabList(sender, arg));
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getInnerType().allowSpaceAfterTab();
	}
	
}
