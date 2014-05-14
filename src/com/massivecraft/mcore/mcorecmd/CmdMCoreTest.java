package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.VisibilityMode;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.IdUtil;

public class CmdMCoreTest extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreTest()
	{
		// Aliases
		this.addAliases("test");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.TEST.node));
		
		// VisibilityMode
		this.setVisibilityMode(VisibilityMode.SECRET);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		IdUtil.isOnline(IdUtil.CONSOLE_ID);
	}
	
}
