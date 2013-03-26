package com.massivecraft.mcore.cmd;

import com.massivecraft.mcore.ConfServer;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Perm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdMcore extends MCommand
{
	public final static String MCORE = "mcore";
	
	public CmdMcore()
	{
		this.addAliases(ConfServer.getCmdAliases(MCORE));
		this.addRequirements(ReqHasPerm.get(Perm.CMD_MCORE.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>You are running %s", MCore.get().getDescription().getFullName());
		this.msg("<i>The id of this server is \"<h>%s<i>\".", ConfServer.serverid);
	}
}