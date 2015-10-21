package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public final class TypeAll
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <T> Type<Collection<T>> get(Type<?> inner)
	{
		if (inner instanceof TypeCollection) return new TypeAllCollection<T>((TypeCollection<? extends Collection<T>>) inner);
		if (inner instanceof TypeAllAble) return new TypeAllDefault<T>((TypeAllAble<T>) inner);
		throw new IllegalArgumentException("innerType must be an instance of either TypeCollection or TypeAllAble");
	}
	
	// -------------------------------------------- //
	// AR ALL DEFAULT
	// -------------------------------------------- //

	private static class TypeAllDefault<T> extends TypeWrapper<Collection<T>>
	{
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
		
		protected TypeAllAble<T> innerType;
		@Override public TypeAllAble<T> getInnerType() { return this.innerType; }
		
		// -------------------------------------------- //
		// CONSTRUCT
		// -------------------------------------------- //
		
		public TypeAllDefault(TypeAllAble<T> inner)
		{
			if (inner == null) throw new IllegalArgumentException("innerType musn't be null");
			this.innerType = inner;
		}
		
		// -------------------------------------------- //
		// OVERRIDE
		// -------------------------------------------- //

		@Override
		public Collection<T> read(String arg, CommandSender sender) throws MassiveException
		{
			if ("all".equalsIgnoreCase(arg)) return getInnerType().getAll(sender);
			
			T innerRet = getInnerType().read(arg, sender);
			return Collections.singletonList(innerRet);
		}
		
		@Override
		public boolean isValid(String arg, CommandSender sender)
		{
			return "all".equalsIgnoreCase(arg) || this.getInnerType().isValid(arg, sender);
		}
		
	}
	
	// -------------------------------------------- //
	// AR ALL COLLECTION
	// -------------------------------------------- //

	private static class TypeAllCollection<T> extends TypeWrapper<Collection<T>>
	{
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
		
		protected TypeCollection<? extends Collection<T>> innerType;
		@Override public TypeCollection<? extends Collection<T>> getInnerType() { return this.innerType; }
		
		// -------------------------------------------- //
		// CONSTRUCT
		// -------------------------------------------- //
		
		public TypeAllCollection(TypeCollection<? extends Collection<T>> inner)
		{
			if (inner == null) throw new IllegalArgumentException("innerType musn't be null");
			if ( !(inner.getInnerType() instanceof TypeAllAble))  throw new IllegalArgumentException("innerType must implement ARAllAble");
			this.innerType = inner;
		}
		
		// -------------------------------------------- //
		// OVERRIDE
		// -------------------------------------------- //

		@Override
		public Collection<T> read(String arg, CommandSender sender) throws MassiveException
		{
			if ("all".equalsIgnoreCase(arg))
			{
				@SuppressWarnings("unchecked")
				TypeAllAble<T> innersInner = (TypeAllAble<T>) getInnerType().getInnerType();
				return innersInner.getAll(sender);
			}
			
			return getInnerType().read(arg, sender);
		}		
		
		@Override
		public boolean isValid(String arg, CommandSender sender)
		{
			return "all".equalsIgnoreCase(arg) || this.getInnerType().isValid(arg, sender);
		}
		
	}

}
