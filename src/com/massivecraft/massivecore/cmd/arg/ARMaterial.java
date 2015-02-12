package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;

public class ARMaterial extends ArgReaderAbstract<Material>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARMaterial i = new ARMaterial();
	public static ARMaterial get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Material read(String arg, CommandSender sender) throws MassiveException
	{
		Material ret = Material.matchMaterial(arg);
		if (ret == null)
		{
			MassiveException exception = new MassiveException();
			exception.addMsg("<b>No material matches <h>%s<b>.", arg);
			exception.addMsg("<i>Suggestion: <aqua>http://www.minecraftwiki.net/wiki/Data_values");
			throw exception;
		}
		
		return ret;
	}
	
}
