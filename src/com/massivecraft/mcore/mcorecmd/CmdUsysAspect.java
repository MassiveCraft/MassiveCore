package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsysAspect extends MCommand
{
	public CmdUsysAspectList cmdUsysAspectList = new CmdUsysAspectList();
	public CmdUsysAspectShow cmdUsysAspectShow = new CmdUsysAspectShow();
	public CmdUsysAspectUse cmdUsysAspectUse = new CmdUsysAspectUse();
	
	public CmdUsysAspect()
	{
		super();
		this.addAliases("a", "aspect");
		
		this.addSubCommand(this.cmdUsysAspectList);
		this.addSubCommand(this.cmdUsysAspectShow);
		this.addSubCommand(this.cmdUsysAspectUse);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_USYS_ASPECT.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
