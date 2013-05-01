package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.cmd.MCommand;

public abstract class MCoreCommand extends MCommand
{	
	public MCoreCommand(List<String> aliases)
	{
		this.setAliases(aliases);
	}
}
