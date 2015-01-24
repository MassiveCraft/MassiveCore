package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommandException;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.Txt;

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
	public Long read(String arg, CommandSender sender)
	{
		Long ret;
		try
		{
			ret = TimeDiffUtil.millis(arg);
		}
		catch (Exception e)
		{
			throw new MassiveCommandException(Txt.parse("<b>") + e.getMessage());
		}
		
		return ret;
	}
	
}
