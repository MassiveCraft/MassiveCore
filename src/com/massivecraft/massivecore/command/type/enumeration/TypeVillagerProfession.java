package com.massivecraft.massivecore.command.type.enumeration;

import org.bukkit.entity.Villager.Profession;

public class TypeVillagerProfession extends TypeEnum<Profession>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeVillagerProfession i = new TypeVillagerProfession();
	public static TypeVillagerProfession get() { return i; }
	public TypeVillagerProfession()
	{
		super(Profession.class);
	}

}
