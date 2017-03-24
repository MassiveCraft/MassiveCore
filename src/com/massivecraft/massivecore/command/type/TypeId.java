package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.enumeration.TypeSound;
import com.massivecraft.massivecore.command.type.primitive.TypeStringId;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

public class TypeId<T> extends TypeTransformer<T, String> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeId<Sound> iSound = TypeId.get(TypeSound.get());
	public static TypeId<Sound> getSound() { return iSound; }
	
	public static <T> TypeId<T> get(Type<T> inner) { return new TypeId<>(inner); }
	public TypeId(Type<T> inner)
	{
		super(inner, TypeStringId.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public String innerToOuter(T inner, CommandSender sender)
	{
		if (inner == null) return null;
		return INNER.getId(inner);
	}

	@Override
	public T outerToInner(String outer)
	{
		if (outer == null) return null;
		try
		{
			return this.INNER.read(outer);
		}
		catch (MassiveException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
}
