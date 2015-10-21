package com.massivecraft.massivecore.cmd.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

public class TypeList<E> extends TypeCollection<List<E>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Type<E> elementType;
	public Type<E> getElementType() { return this.elementType; }
	@Override public Type<E> getInnerType() { return this.getElementType(); }
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static <E> TypeList<E> get(Type<E> elementType)
	{
		return new TypeList<E>(elementType);
	}
	
	public TypeList(Type<E> elementType)
	{
		this.elementType = elementType;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	// NOTE: Must be used with argConcatFrom and setErrorOnTooManyArgs(false).
	@Override
	public List<E> read(String arg, CommandSender sender) throws MassiveException
	{
		// Split into inner args
		String[] elementArgs = Txt.REGEX_WHITESPACE.split(arg);
		
		// Create Ret
		List<E> ret = new ArrayList<E>();
		
		// For Each
		for (String elementArg : elementArgs)
		{
			E element = this.getElementType().read(elementArg, sender);
			
			ret.add(element);
		}
		
		// Return Ret
		return ret;
	}

}
