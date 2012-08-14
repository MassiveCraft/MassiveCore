package com.massivecraft.mcore4.cmd.arg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AHDate extends AHPrimitive<Date>
{	
	protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	protected String getPrimitiveName()
	{
		return "YYYY-MM-DD date";
	}

	@Override
	protected Date unsafeConvert(String str) throws Exception
	{
		return df.parse(str);
	}	
}