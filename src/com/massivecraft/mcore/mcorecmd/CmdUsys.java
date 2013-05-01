package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.ConfServer;
import com.massivecraft.mcore.MCorePerm;
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
		
		this.addAliases(ConfServer.getCmdAliases(USYS));
		
		this.addSubCommand(this.cmdUsysMultiverse);
		this.addSubCommand(this.cmdUsysUniverse);
		this.addSubCommand(this.cmdUsysWorld);
		this.addSubCommand(this.cmdUsysAspect);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_USYS.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}