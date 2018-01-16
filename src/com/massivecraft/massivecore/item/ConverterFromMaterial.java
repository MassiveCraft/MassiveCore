package com.massivecraft.massivecore.item;

import org.bukkit.Material;

public class ConverterFromMaterial extends Converter<Material, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static final ConverterFromMaterial i = new ConverterFromMaterial();
	public static ConverterFromMaterial get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String convert(Material x)
	{
		if (x == null) return null;
		return x.name();
	}

}
