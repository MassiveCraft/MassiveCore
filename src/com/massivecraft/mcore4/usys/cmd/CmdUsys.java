package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.HelpCommand;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;

public class CmdUsys extends UsysCommand
{	
	public CmdUsysMultiverse cmdUsysMultiverse = new CmdUsysMultiverse();
	public CmdUsysUniverse cmdUsysUniverse = new CmdUsysUniverse();
	public CmdUsysWorld cmdUsysWorld = new CmdUsysWorld();
	public CmdUsysAspect cmdUsysAspect = new CmdUsysAspect();
	
	public CmdUsys()
	{
		super();
		
		// TODO: Make configurable
		this.addAliases("usys");
		
		this.addSubCommand(this.cmdUsysMultiverse);
		this.addSubCommand(this.cmdUsysUniverse);
		this.addSubCommand(this.cmdUsysWorld);
		this.addSubCommand(this.cmdUsysAspect);
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}