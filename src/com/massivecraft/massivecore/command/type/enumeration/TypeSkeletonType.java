package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Skeleton.SkeletonType;

public class TypeSkeletonType extends TypeEnum<SkeletonType>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSkeletonType i = new TypeSkeletonType();
	public static TypeSkeletonType get() { return i; }
	public TypeSkeletonType()
	{
		super(SkeletonType.class);
	}

}
