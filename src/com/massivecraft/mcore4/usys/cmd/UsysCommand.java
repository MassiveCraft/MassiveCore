package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.cmd.MCommand;

public abstract class UsysCommand extends MCommand
{
	public MCore p;
	public UsysCommand()
	{
		super();
		this.p = MCore.p;
	}
	
	@Override
	public MCore p()
	{
		return MCore.p;
	}
}
