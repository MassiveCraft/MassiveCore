package com.massivecraft.mcore5.usys.cmd;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.HelpCommand;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;

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
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_ASPECT.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
