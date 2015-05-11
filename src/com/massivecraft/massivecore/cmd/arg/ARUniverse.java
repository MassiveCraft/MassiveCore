package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.Txt;

public class ARUniverse extends ARAbstract<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Aspect aspect = null;
	protected Multiverse multiverse = null;
	
	public Multiverse getMultiverse()
	{
		if (this.aspect != null) return this.aspect.getMultiverse();
		return this.multiverse;
	}
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ARUniverse get(Aspect aspect) { return new ARUniverse(aspect); }
	public static ARUniverse get(Multiverse multiverse) { return new ARUniverse(multiverse); }
	
	public ARUniverse(Aspect aspect) { this.aspect = aspect; }
	public ARUniverse(Multiverse multiverse) { this.multiverse = multiverse; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String read(String arg, CommandSender sender) throws MassiveException
	{
		Multiverse multiverse = this.getMultiverse();
		
		if (multiverse.containsUniverse(arg))
		{
			return arg;
		}
		else
		{
			Collection<String> names = new ArrayList<String>(multiverse.getUniverses());
			String format = Txt.parse("<h>%s");
			String comma = Txt.parse("<i>, ");
			String and = Txt.parse(" <i>or ");
			String dot = Txt.parse("<i>.");
			
			throw new MassiveException()
			.addMsg("<b>No universe \"<h>%s<b>\" exists in multiverse <h>%s<b>.", arg, multiverse.getId())
			.addMsg("<i>Use %s", Txt.implodeCommaAndDot(names, format, comma, and, dot));
		}
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		// MassiveSet is linked.
		return new MassiveSet<String>(multiverse.getUniverses());
	}

}
