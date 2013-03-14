package com.massivecraft.mcore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.TimeDiffUtil;
import com.massivecraft.mcore.util.Txt;

public class ARMillisDiff extends ArgReaderAbstract<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARMillisDiff i = new ARMillisDiff();
	public static ARMillisDiff get() { return i; }
	
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
	
}
