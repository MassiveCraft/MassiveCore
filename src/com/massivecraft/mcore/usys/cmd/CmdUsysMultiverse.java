package com.massivecraft.mcore.usys.cmd;

import com.massivecraft.mcore.Perm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsysMultiverse extends MCommand
{
	public CmdUsysMultiverseList cmdUsysMultiverseList = new CmdUsysMultiverseList();
	public CmdUsysMultiverseShow cmdUsysMultiverseShow = new CmdUsysMultiverseShow();
	public CmdUsysMultiverseNew cmdUsysMultiverseNew = new CmdUsysMultiverseNew();
	public CmdUsysMultiverseDel cmdUsysMultiverseDel = new CmdUsysMultiverseDel();
	
	public CmdUsysMultiverse()
	{
		super();
		this.addAliases("m", "multiverse");
		
		this.addSubCommand(this.cmdUsysMultiverseList);
		this.addSubCommand(this.cmdUsysMultiverseShow);
		this.addSubCommand(this.cmdUsysMultiverseNew);
		this.addSubCommand(this.cmdUsysMultiverseDel);
		
		this.addRequirements(ReqHasPerm.get(Perm.CMD_USYS_MULTIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
