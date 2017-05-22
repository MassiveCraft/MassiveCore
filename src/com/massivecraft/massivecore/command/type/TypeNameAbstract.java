package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class TypeNameAbstract extends TypeAbstract<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private final boolean strict;
	public boolean isStrict() { return this.strict; }
	public boolean isLenient() { return ! this.isStrict(); }
	
	private Integer lengthMin = 1;
	public Integer getLengthMin() { return this.lengthMin; }
	public void setLengthMin(Integer lengthMin) { this.lengthMin = lengthMin; }
	
	private Integer lengthMax = null;
	public Integer getLengthMax() { return this.lengthMax; }
	public void setLengthMax(Integer lengthMax) { this.lengthMax = lengthMax; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public TypeNameAbstract(boolean strict)
	{
		super(String.class);
		this.strict = strict;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String read(String arg, CommandSender sender) throws MassiveException
	{
		if (arg == null) throw new NullPointerException("arg");

		// Allow changing capitalization of the current name if lenient.
		String current = this.getCurrentName(sender);
		if (current != null && current.equalsIgnoreCase(arg) && this.isLenient()) return arg;

		if (this.isNameTaken(arg)) throw new MassiveException().addMsg("<b>The name \"<h>%s<b>\" is already in use.",arg);
		
		Integer lengthMin = this.getLengthMin();
		if (lengthMin != null && arg.length() < lengthMin)
		{
			throw new MassiveException().addMsg("<b>The name must be at least <h>%d<b> characters.", lengthMin);
		}
		
		Integer lengthMax = this.getLengthMax();
		if (lengthMax != null && arg.length() >lengthMax)
		{
			throw new MassiveException().addMsg("<b>The name must be at most <h>%d<b> characters.", lengthMax);
		}
		
		Set<Character> disallowed = new MassiveSet<>();
		for (char character : arg.toCharArray())
		{
			if (!this.isCharacterAllowed(character)) disallowed.add(character);
		}
		
		// We found some disallowed characters
		if (!disallowed.isEmpty())
		{
			String characterViolations = Txt.implode(disallowed, "");
			String pluralityResolution = disallowed.size() == 1 ? " is" : "s are";
			throw new MassiveException().addMsg("<b>The following character%s not allowed: <h>%s<b>.", pluralityResolution, characterViolations);
		}
		
		return arg;
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptyList();
	}
	
	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public String getCurrentName(CommandSender sender)
	{
		Named named = this.getCurrent(sender);
		if (named == null) return null;
		return named.getName();
	}
	
	// Override this if you want to specify what characters may be used
	public boolean isCharacterAllowed(char character) { return true; }
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Named getCurrent(CommandSender sender);
	
	public abstract boolean isNameTaken(String name);
	
}
