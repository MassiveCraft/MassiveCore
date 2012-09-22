package com.massivecraft.mcore4.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;

import com.massivecraft.mcore4.cmd.MCommand;
import com.massivecraft.mcore4.usys.Multiverse;
import com.massivecraft.mcore4.util.Txt;

public class ARUniverse implements ArgReader<String>
{
	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override
	public ArgResult<String> read(String str, MCommand mcommand)
	{
		ArgResult<String> result = new ArgResult<String>();
		
		if (multiverse.containsUniverse(str))
		{
			result.setResult(str);
		}
		else
		{
			result.getErrors().add("<b>No universe \"<h>"+str+"<b>\" exists in multiverse <h>"+this.multiverse.getId()+"<b>.");
			
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
