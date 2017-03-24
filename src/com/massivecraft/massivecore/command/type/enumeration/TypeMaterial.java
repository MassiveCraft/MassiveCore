package com.massivecraft.massivecore.command.type.enumeration;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Material;

import java.util.Set;

public class TypeMaterial extends TypeEnum<Material>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMaterial i = new TypeMaterial();
	public static TypeMaterial get() { return i; }
	public TypeMaterial()
	{
		super(Material.class);
		this.setHelp(
			Txt.parse("<aqua>https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/Material.java")
		);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Set<String> getIdsInner(Material value)
	{
		Set<String> ret = new MassiveSet<>(super.getIdsInner(value));
		
		String id = String.valueOf(value.getId());
		ret.add(id);
		
		return ret;
	}

}
