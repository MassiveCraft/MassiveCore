package com.massivecraft.massivecore.command.type;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;

public abstract class TypeNameAbstract extends TypeAbstract<String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //

	private final boolean strict;
	public boolean isStrict() { return this.strict; }
	public boolean isLenient() { return ! this.isStrict(); }

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public TypeNameAbstract(boolean strict)
	{
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
		
		return arg;
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptyList();
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract String getCurrentName(CommandSender sender);
	
	public abstract boolean isNameTaken(String name);
	
}
