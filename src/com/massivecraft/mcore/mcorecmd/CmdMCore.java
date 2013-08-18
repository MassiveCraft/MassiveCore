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
	public CmdMCoreUsys cmdMCoreUsys = new CmdMCoreUsys(MUtil.list("usys"));
	public CmdMCoreMStore cmdMCoreMStore = new CmdMCoreMStore(MUtil.list("mstore"));
	public CmdMCoreId cmdMCoreId = new CmdMCoreId(MUtil.list("id"));
	public CmdMCoreHearsound cmdMCoreHearsound = new CmdMCoreHearsound(MUtil.list("hearsound", "hearsounds"));
	public VersionCommand cmdMCoreVersion = new VersionCommand(MCore.get(), MCorePerm.CMD_MCORE_VERSION.node, "v", "version");
	
	
	public CmdMCore(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdMCoreUsys);
		this.addSubCommand(this.cmdMCoreMStore);
		this.addSubCommand(this.cmdMCoreId);
		this.addSubCommand(this.cmdMCoreHearsound);
		this.addSubCommand(this.cmdMCoreVersion);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}