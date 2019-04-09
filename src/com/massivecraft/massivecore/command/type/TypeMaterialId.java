package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.command.type.enumeration.TypeMaterial;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public class TypeMaterialId extends TypeTransformer<Material, String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMaterialId i = new TypeMaterialId();
	public static TypeMaterialId get() { return i; }
	
	public TypeMaterialId()
	{
		super(TypeMaterial.get(), TypeString.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	

	@Override
	public String innerToOuter(Material inner, CommandSender sender)
	{
		if (inner == null) return null;
		return inner.toString();
	}

	@Override
	public Material outerToInner(String outer)
	{
		if (outer == null) return null;
		return Material.getMaterial(outer);
	}
	
}
