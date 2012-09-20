package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.HelpCommand;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;

public class CmdUsysMultiverse extends UsysCommand
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
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_MULTIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
