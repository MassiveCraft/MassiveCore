package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCoreUsysAspect extends MCoreCommand
{
	public CmdMCoreUsysAspectList cmdUsysAspectList = new CmdMCoreUsysAspectList(MUtil.list("l", "list"));
	public CmdMCoreUsysAspectShow cmdUsysAspectShow = new CmdMCoreUsysAspectShow(MUtil.list("s", "show"));
	public CmdMCoreUsysAspectUse cmdUsysAspectUse = new CmdMCoreUsysAspectUse(MUtil.list("u", "use"));
	
	public CmdMCoreUsysAspect(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdUsysAspectList);
		this.addSubCommand(this.cmdUsysAspectShow);
		this.addSubCommand(this.cmdUsysAspectUse);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
