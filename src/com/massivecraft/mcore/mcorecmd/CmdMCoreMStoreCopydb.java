package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreMStoreCopydb extends MCoreCommand
{
	public CmdMCoreMStoreCopydb(List<String> aliases)
	{
		super(aliases);
		
		this.addRequiredArg("fromuri");
		this.addRequiredArg("touri");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE_COPYDB.node));
	}
	
	@Override
	public void perform()
	{
		// TODO
	}
	
}
