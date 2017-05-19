package com.massivecraft.massivecore.command.type.store;

import com.massivecraft.massivecore.command.type.TypeAbstractChoice;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.SenderColl;

import java.util.Collection;

public class TypeSenderColl extends TypeAbstractChoice<SenderColl<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSenderColl i = new TypeSenderColl();
	public static TypeSenderColl get() { return i; }
	public TypeSenderColl() { super(Coll.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Collection<SenderColl<?>> getAll()
	{
		return SenderColl.getSenderInstances();
	}
	
}
