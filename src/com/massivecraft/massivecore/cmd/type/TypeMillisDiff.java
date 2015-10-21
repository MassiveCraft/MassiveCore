package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.TimeDiffUtil;

public class TypeMillisDiff extends TypeAbstractException<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMillisDiff i = new TypeMillisDiff();
	public static TypeMillisDiff get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getTypeName()
	{
		return "time amount";
	}
	
	@Override
	public Long valueOf(String arg, CommandSender sender) throws Exception
	{
		return TimeDiffUtil.millis(arg);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return Collections.emptySet();
	}

}
