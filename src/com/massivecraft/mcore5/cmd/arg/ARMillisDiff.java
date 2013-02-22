package com.massivecraft.mcore5.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore5.util.TimeDiffUtil;
import com.massivecraft.mcore5.util.Txt;

public class ARMillisDiff implements ArgReader<Long>
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<Long> read(String arg, CommandSender sender)
	{
		ArgResult<Long> ret = new ArgResult<Long>();
		try
		{
			ret.setResult(TimeDiffUtil.millis(arg));
		}
		catch (Exception e)
		{
			ret.setErrors(Txt.parse("<b>")+e.getMessage());
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARMillisDiff i = new ARMillisDiff();
	public static ARMillisDiff get() { return i; }
	
}
