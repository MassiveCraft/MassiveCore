package com.massivecraft.massivecore.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.TimeDiffUtil;

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
	public Long read(String arg, CommandSender sender) throws MassiveException
	{
		Long ret;
		try
		{
			ret = TimeDiffUtil.millis(arg);
		}
		catch (Exception e)
		{
			throw new MassiveException().addMsg("<b>%s", e.getMessage());
		}
		
		return ret;
	}
	
}
