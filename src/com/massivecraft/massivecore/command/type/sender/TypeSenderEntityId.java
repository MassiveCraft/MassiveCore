package com.massivecraft.massivecore.command.type.sender;

import com.massivecraft.massivecore.command.type.TypeTransformer;
import com.massivecraft.massivecore.command.type.primitive.TypeStringId;
import com.massivecraft.massivecore.store.SenderEntity;
import org.bukkit.command.CommandSender;

public class TypeSenderEntityId<I extends SenderEntity<I>> extends TypeTransformer<I, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public TypeSenderEntityId(TypeSenderEntity<I> typeInner)
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
		TypeSenderEntity<I> typeSenderEntity = (TypeSenderEntity<I>)INNER;
		return typeSenderEntity.getColl().get(outer, false);
	}

}
