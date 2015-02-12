package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
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
	public String read(String arg, CommandSender sender) throws MassiveException
	{
		String result = new String();
		
		if (multiverse.containsUniverse(arg))
		{
			result = arg;
		}
		else
		{
			MassiveException exception = new MassiveException();
			exception.addMsg("<b>No universe \"<h>%s<b>\" exists in multiverse <h>%s<b>.", arg, this.multiverse.getId());
			
			Collection<String> names = new ArrayList<String>(multiverse.getUniverses());
			String format = Txt.parse("<h>%s");
			String comma = Txt.parse("<i>, ");
			String and = Txt.parse(" <i>or ");
			String dot = Txt.parse("<i>.");
			exception.addMsg("<i>Use %s", Txt.implodeCommaAndDot(names, format, comma, and, dot));
			
			throw exception;
		}
		
		return result;
	}
	
}
