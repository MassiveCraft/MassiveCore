package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.ConfServer;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMCoreId extends MCoreCommand
{
	public CmdMCoreId(List<String> aliases)
	{
		super(aliases);
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_ID.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>The id of this server is \"<h>%s<i>\".", ConfServer.serverid);
	}
}
