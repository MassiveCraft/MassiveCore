package com.massivecraft.massivecore.command.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.bukkit.command.CommandSender;

public class TypeDate extends TypeAbstractSimple<Date>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeDate i = new TypeDate();
	public static TypeDate get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "YYYY-MM-DD date";
	}

	@Override
	public String getIdInner(Date value)
	{
		return DATE_FORMAT.format(value);
	}
	
	@Override
	public Date valueOf(String arg, CommandSender sender) throws Exception
	{
		return DATE_FORMAT.parse(arg);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptySet();
	}	

}
