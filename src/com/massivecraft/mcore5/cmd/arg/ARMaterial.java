package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.Material;

import com.massivecraft.mcore5.cmd.MCommand;

public class ARMaterial implements ArgReader<Material>
{
	@Override
	public ArgResult<Material> read(String str, MCommand mcommand)
	{
		ArgResult<Material> result = new ArgResult<Material>(Material.matchMaterial(str));
		if (!result.hasResult())
		{
			result.getErrors().add("<b>No material matches <h>"+str+"<b>.");
			result.getErrors().add("<i>Suggestion: <aqua>http://www.minecraftwiki.net/wiki/Data_values");
		}
		return result;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARMaterial i = new ARMaterial();
	public static ARMaterial get() { return i; }
	
}
