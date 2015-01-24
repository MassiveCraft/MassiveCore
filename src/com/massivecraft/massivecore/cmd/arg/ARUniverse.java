package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.util.Txt;

public class ARUniverse extends ArgReaderAbstract<String>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public ARUniverse(Multiverse multiverse)
	{
		this.multiverse = multiverse;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Multiverse multiverse;
	public Multiverse multiverse() { return this.multiverse; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String read(String arg, CommandSender sender)
	{
		String result = new String();
		
		if (multiverse.containsUniverse(arg))
		{
			result = arg;
		}
		else
		{
			MassiveCommandException errors = new MassiveCommandException();
			errors.addErrorMsg("<b>No universe \"<h>" + arg + "<b>\" exists in multiverse <h>" + this.multiverse.getId() + "<b>.");
			Collection<String> names = new ArrayList<String>(multiverse.getUniverses());
			errors.addErrorMsg("<i>Use " + Txt.implodeCommaAndDot(names, "<h>%s", "<i>, ", " <i>or ", "<i>."));
			throw errors;
		}
		
		return result;
	}
	
}
