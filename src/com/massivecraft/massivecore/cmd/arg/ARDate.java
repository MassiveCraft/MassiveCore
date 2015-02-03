package com.massivecraft.massivecore.cmd.arg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.bukkit.command.CommandSender;

public class ARDate extends ARAbstractPrimitive<Date>
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARDate i = new ARDate();
	public static ARDate get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "YYYY-MM-DD date";
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
