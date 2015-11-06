package com.massivecraft.massivecore.command.type;

import java.util.Collection;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.Multiverse;

public class TypeUniverse extends TypeAbstractChoice<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Aspect aspect = null;
	protected Multiverse multiverse = null;
	
	public Multiverse getMultiverse()
	{
		if (this.aspect != null) return this.aspect.getMultiverse();
		return this.multiverse;
	}
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static TypeUniverse get(Aspect aspect) { return new TypeUniverse(aspect); }
	public static TypeUniverse get(Multiverse multiverse) { return new TypeUniverse(multiverse); }
	
	public TypeUniverse(Aspect aspect) { this.aspect = aspect; }
	public TypeUniverse(Multiverse multiverse) { this.multiverse = multiverse; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public Collection<String> getAll()
	{
		return this.getMultiverse().getUniverses();
	}

}
