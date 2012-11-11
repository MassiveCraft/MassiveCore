package com.massivecraft.mcore5.usys.cmd;

import com.massivecraft.mcore5.Conf;
import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.HelpCommand;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;

public class CmdUsys extends UsysCommand
{
	public final static String USYS = "usys";
	
	public CmdUsysMultiverse cmdUsysMultiverse = new CmdUsysMultiverse();
	public CmdUsysUniverse cmdUsysUniverse = new CmdUsysUniverse();
	public CmdUsysWorld cmdUsysWorld = new CmdUsysWorld();
	public CmdUsysAspect cmdUsysAspect = new CmdUsysAspect();
	
	public CmdUsys()
	{
		super();
		
		this.addAliases(Conf.cmdaliases.get(USYS));
		
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