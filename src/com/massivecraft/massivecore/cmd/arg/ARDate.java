package com.massivecraft.massivecore.cmd.arg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	public String typename()
	{
		return "YYYY-MM-DD date";
	}

	@Override
	public Date convert(String arg) throws Exception
	{
		return DATE_FORMAT.parse(arg);
	}
	
}