package com.massivecraft.mcore5.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.usys.Multiverse;
import com.massivecraft.mcore5.util.Txt;

public class ARUniverse implements ArgReader<String>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public ArgResult<String> read(String arg, CommandSender sender)
	{
		ArgResult<String> result = new ArgResult<String>();
		
		if (multiverse.containsUniverse(arg))
		{
			result.setResult(arg);
		}
		else
		{
			result.getErrors().add("<b>No universe \"<h>"+arg+"<b>\" exists in multiverse <h>"+this.multiverse.getId()+"<b>.");
			
			Collection<String> names = new ArrayList<String>(multiverse.getUniverses());
			result.getErrors().add("<i>Use "+Txt.implodeCommaAndDot(names, "<h>%s", "<i>, ", " <i>or ", "<i>."));
		}
		
		return result;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Multiverse multiverse;
	public Multiverse multiverse() { return this.multiverse; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARUniverse(Multiverse multiverse)
	{
		this.multiverse = multiverse;
	}
}
