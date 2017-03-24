package com.massivecraft.massivecore.command.type;

import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class TypeMillisDiff extends TypeAbstractException<Long>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeMillisDiff i = new TypeMillisDiff();
	public static TypeMillisDiff get() { return i; }
	public TypeMillisDiff() { super(Long.class); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getName()
	{
		return "time amount";
	}
	
	@Override
	public String getVisualInner(Long value, CommandSender sender)
	{
		String prefix = Txt.parse(value < 0 ? "<b>negative " : "");
		LinkedHashMap<TimeUnit, Long> unitcounts = TimeDiffUtil.unitcounts(value);
		return prefix + TimeDiffUtil.formatedVerboose(unitcounts);
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
