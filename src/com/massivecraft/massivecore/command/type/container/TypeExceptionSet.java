package com.massivecraft.massivecore.command.type.container;

import java.util.Collection;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.editor.Property;
import com.massivecraft.massivecore.command.editor.PropertyReflection;
import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.command.type.primitive.TypeBoolean;
import com.massivecraft.massivecore.util.ReflectionUtil;
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
	
	@SuppressWarnings("unchecked")
	public TypeExceptionSet(final Type<E> innerType)
	{
		super(ExceptionSet.class);
		
		this.typeElements = TypeSet.get(innerType);
		
		// PROPERTIES
		Property<ExceptionSet<E>, Boolean> propertyStandard = PropertyReflection.get(ReflectionUtil.getField(ExceptionSet.class, "standard"), this);
		
		Property<ExceptionSet<E>, Set<E>> propertyExceptions = new Property<ExceptionSet<E>, Set<E>>(this, typeElements, "exceptions")
		{
			@Override
			public Set<E> getRaw(ExceptionSet<E> object)
			{
				Set<E> ret = new MassiveSet<>();
				
				for (String exception : object.exceptions)
				{
					try
					{
						ret.add(innerType.read(exception));
					}
					catch (MassiveException e)
					{
						e.printStackTrace();
					}
				}
				
				return ret;
			}

			@Override
			public ExceptionSet<E> setRaw(ExceptionSet<E> object, Set<E> value)
			{
				object.exceptions = object.asStrings(value);
				return object;
				
			}
		};
		propertyExceptions.setNullable(false);
		
		this.setInnerProperties(propertyStandard, propertyExceptions);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	// TODO: Do we even need this now?
	
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
