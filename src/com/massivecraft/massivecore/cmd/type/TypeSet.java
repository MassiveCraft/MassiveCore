package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.Txt;

public class TypeSet<E> extends TypeCollection<Set<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected final Type<E> elementType;
	public Type<E> getElementType() { return this.elementType; }
	@Override public Type<E> getInnerType() { return this.getElementType(); }
	
	protected final boolean warnOnDuplicates;
	public boolean getWarnOnDuplicate() { return warnOnDuplicates; }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> TypeSet<E> get(Type<E> elementType, boolean warnOnDuplicates)
	{
		return new TypeSet<E>(elementType, warnOnDuplicates);
	}
	
	public TypeSet(Type<E> elementType, boolean warnOnDuplicates)
	{
		this.elementType = elementType;
		this.warnOnDuplicates = warnOnDuplicates;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return elementType.getTypeName();
	}

	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public Set<E> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] elementArgs = Txt.REGEX_WHITESPACE.split(arg);
		
		// Create Ret
		Set<E> ret = new LinkedHashSet<E>();
		
		boolean duplicates = false;
		
		// For Each
		for (String elementArg : elementArgs)
		{
			E element = this.getElementType().read(elementArg, sender);
			
			duplicates = ( ! ret.add(element) || duplicates);
		}
		
		if (warnOnDuplicates && duplicates)
		{
			Mixin.msgOne(sender, "<i>Some duplicate command input were removed.");
		}
		
		// Return Ret
		return ret;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return elementType.getTabList(sender, arg);
	}

}
