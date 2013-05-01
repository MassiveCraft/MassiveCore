package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.VersionCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCore extends MCoreCommand
{
	public CmdMCoreId cmdMCoreId = new CmdMCoreId(MUtil.list("id"));
	public CmdMCoreUsys cmdMCoreUsys = new CmdMCoreUsys(MUtil.list("usys"));
	
	public CmdMCore(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdMCoreUsys);
		this.addSubCommand(this.cmdMCoreId);
		this.addSubCommand(new VersionCommand(MCore.get(), MCorePerm.CMD_MCORE_VERSION.node, "v", "version"));
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}