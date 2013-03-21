package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.Conf;
import com.massivecraft.mcore.Perm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsys extends MCommand
{
	public final static String USYS = "usys";
	
	public CmdUsysMultiverse cmdUsysMultiverse = new CmdUsysMultiverse();
	public CmdUsysUniverse cmdUsysUniverse = new CmdUsysUniverse();
	public CmdUsysWorld cmdUsysWorld = new CmdUsysWorld();
	public CmdUsysAspect cmdUsysAspect = new CmdUsysAspect();
	
	public CmdUsys()
	{
		super();
		
		this.addAliases(Conf.getCmdAliases(USYS));
		
		this.addSubCommand(this.cmdUsysMultiverse);
		this.addSubCommand(this.cmdUsysUniverse);
		this.addSubCommand(this.cmdUsysWorld);
		this.addSubCommand(this.cmdUsysAspect);
		
		this.addRequirements(ReqHasPerm.get(Perm.CMD_USYS.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}