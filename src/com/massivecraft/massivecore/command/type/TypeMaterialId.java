package com.massivecraft.massivecore.command.type;

import org.bukkit.Material;

import com.massivecraft.massivecore.command.type.enumeration.TypeMaterial;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;

@SuppressWarnings("deprecation")
public class TypeMaterialId extends TypeTransformer<Material, Integer> 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMaterialId i = new TypeMaterialId();
	public static TypeMaterialId get() { return i; }
	
	public TypeMaterialId()
	{
		super(TypeMaterial.get(), TypeInteger.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public Integer innerToOuter(Material inner)
	{
		if (inner == null) return null;
		return inner.getId();
	}

	@Override
	public Material outerToInner(Integer outer)
	{
		if (outer == null) return null;
		return Material.getMaterial(outer);
	}
	
}
