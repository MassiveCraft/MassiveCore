package com.massivecraft.mcore5.cmd;

import com.massivecraft.mcore5.Conf;
import com.massivecraft.mcore5.MCore;
import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;

public class CmdMcore extends MCommand
{
	public final static String MCORE = "mcore";
	
	public CmdMcore()
	{
		this.addAliases(Conf.getCmdAliases(MCORE));
		this.addRequirements(ReqHasPerm.get(Permission.CMD_MCORE.node));
	}
	
	@Override
	public void perform()
	{
		this.msg("<i>You are running %s", MCore.p.getDescription().getFullName());
		this.msg("<i>The id of this server is \"<h>%s<i>\".", Conf.serverid);
	}
	
	@Override
	public MCore p()
	{
		return MCore.p;
	}
}