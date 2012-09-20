package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.HelpCommand;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;

public class CmdUsysAspect extends UsysCommand
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
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_ASPECT.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
