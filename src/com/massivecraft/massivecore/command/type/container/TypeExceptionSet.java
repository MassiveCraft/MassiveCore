package com.massivecraft.massivecore.command.type.container;

import java.util.Collection;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.util.Txt;

public class TypeExceptionSet<E> extends TypeAbstract<ExceptionSet<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final TypeSet<E> typeElements;
	public TypeSet<E> getTypeElements() { return this.typeElements; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> TypeExceptionSet<E> get(Type<E> innerType)
	{
		return new TypeExceptionSet<E>(innerType);
	}
	
	public TypeExceptionSet(Type<E> innerType)
	{
		this.typeElements = TypeSet.get(innerType);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ExceptionSet<E> read(String arg, CommandSender sender) throws MassiveException
	{
		String[] args = Txt.PATTERN_WHITESPACE.split(arg, 2);
		String first = args[0];
		String second = args.length == 2 ? args[1] : "";
		
		boolean standard = TypeBoolean.getTrue().read(first, sender);
		Set<E> exceptions = this.getTypeElements().read(second, sender);
		
		ExceptionSet<E> ret = new ExceptionSet<>();
		ret.standard = standard;
		
		for (E exception: exceptions)
		{
			ret.exceptions.add(ret.convert(exception));
		}
		
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		if (arg.contains(" "))
		{
			return this.getTypeElements().getTabList(sender, arg.substring(arg.indexOf(' ')));
		}
		else
		{
			return TypeBoolean.getTrue().getTabList(sender, arg);
		}
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return this.getTypeElements().allowSpaceAfterTab();
	}

}
