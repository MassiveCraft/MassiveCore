package com.massivecraft.massivecore.command.type.store;

import com.massivecraft.massivecore.command.type.TypeTransformer;
import com.massivecraft.massivecore.command.type.primitive.TypeStringId;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.command.CommandSender;

public class TypeEntityId<I extends Entity<I>> extends TypeTransformer<I, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public TypeEntityId(TypeEntity<I> typeInner)
	{
		super(typeInner, TypeStringId.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String innerToOuter(I inner, CommandSender sender)
	{
		if (inner == null) return null;
		return inner.getId();
	}

	@Override
	public I outerToInner(String outer)
	{
		if (outer == null) return null;
		TypeEntity<I> typeEntity = (TypeEntity<I>)this.getInner();
		return typeEntity.getColl().get(outer, false);
	}

}
