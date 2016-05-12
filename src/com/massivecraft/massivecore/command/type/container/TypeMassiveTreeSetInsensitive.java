package com.massivecraft.massivecore.command.type.container;

import com.massivecraft.massivecore.collections.MassiveTreeSet;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;

public class TypeMassiveTreeSetInsensitive extends TypeContainer<MassiveTreeSet<String, ComparatorCaseInsensitive>, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final TypeMassiveTreeSetInsensitive i = new TypeMassiveTreeSetInsensitive();
	public static TypeMassiveTreeSetInsensitive get() { return i; }
	public TypeMassiveTreeSetInsensitive()
	{
		super(MassiveTreeSet.class, TypeString.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public MassiveTreeSet<String, ComparatorCaseInsensitive> createNewInstance()
	{
		return new MassiveTreeSet<>(ComparatorCaseInsensitive.get());
	}

}
