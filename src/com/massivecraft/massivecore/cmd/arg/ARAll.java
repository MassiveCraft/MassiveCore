package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public final class ARAll
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <T> AR<Collection<T>> get(AR<?> inner)
	{
		if (inner instanceof ARCollection) return new ARAllCollection<T>((ARCollection<? extends Collection<T>>) inner);
		if (inner instanceof ARAllAble) return new ARAllDefault<T>((ARAllAble<T>) inner);
		throw new IllegalArgumentException("innerArgReader must be an instance of either ARCollection or ARAllAble");
	}
	
	// -------------------------------------------- //
	// AR ALL DEFAULT
	// -------------------------------------------- //

	private static class ARAllDefault<T> extends ARWrapper<Collection<T>>
	{
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
		
		protected ARAllAble<T> innerArgReader;
		@Override public ARAllAble<T> getInnerArgReader() { return this.innerArgReader; }
		
		// -------------------------------------------- //
		// CONSTRUCT
		// -------------------------------------------- //
		
		public ARAllDefault(ARAllAble<T> inner)
		{
			if (inner == null) throw new IllegalArgumentException("innerArgReader musn't be null");
			this.innerArgReader = inner;
		}
		
		// -------------------------------------------- //
		// OVERRIDE
		// -------------------------------------------- //

		@Override
		public Collection<T> read(String arg, CommandSender sender) throws MassiveException
		{
			if ("all".equalsIgnoreCase(arg)) return getInnerArgReader().getAll(sender);
			
			T innerRet = getInnerArgReader().read(arg, sender);
			return Collections.singletonList(innerRet);
		}
		
		@Override
		public boolean isValid(String arg, CommandSender sender)
		{
			return "all".equalsIgnoreCase(arg) || this.getInnerArgReader().isValid(arg, sender);
		}
		
	}
	
	// -------------------------------------------- //
	// AR ALL COLLECTION
	// -------------------------------------------- //

	private static class ARAllCollection<T> extends ARWrapper<Collection<T>>
	{
		// -------------------------------------------- //
		// FIELDS
		// -------------------------------------------- //
		
		protected ARCollection<? extends Collection<T>> innerArgReader;
		@Override public ARCollection<? extends Collection<T>> getInnerArgReader() { return this.innerArgReader; }
		
		// -------------------------------------------- //
		// CONSTRUCT
		// -------------------------------------------- //
		
		public ARAllCollection(ARCollection<? extends Collection<T>> inner)
		{
			if (inner == null) throw new IllegalArgumentException("innerArgReader musn't be null");
			if ( !(inner.getInnerArgReader() instanceof ARAllAble))  throw new IllegalArgumentException("innerArgReaders innerArgReader must implement ARAllAble");
			this.innerArgReader = inner;
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
				ARAllAble<T> innersInner = (ARAllAble<T>) getInnerArgReader().getInnerArgReader();
				return innersInner.getAll(sender);
			}
			
			return getInnerArgReader().read(arg, sender);
		}		
		
		@Override
		public boolean isValid(String arg, CommandSender sender)
		{
			return "all".equalsIgnoreCase(arg) || this.getInnerArgReader().isValid(arg, sender);
		}
		
	}

}
