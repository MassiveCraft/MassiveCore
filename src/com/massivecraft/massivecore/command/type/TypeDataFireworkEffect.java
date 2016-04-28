package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.command.type.primitive.TypeObject;
import com.massivecraft.massivecore.item.DataFireworkEffect;

public class TypeDataFireworkEffect extends TypeObject<DataFireworkEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDataFireworkEffect i = new TypeDataFireworkEffect();
	public static TypeDataFireworkEffect get() { return i; }
	public TypeDataFireworkEffect()
	{
		super(DataFireworkEffect.class);
	}

}
