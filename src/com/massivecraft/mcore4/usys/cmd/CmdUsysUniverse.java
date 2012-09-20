package com.massivecraft.mcore4.usys.cmd;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.HelpCommand;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;

public class CmdUsysUniverse extends UsysCommand
{
	public CmdUsysUniverseNew cmdUsysUniverseNew = new CmdUsysUniverseNew();
	public CmdUsysUniverseDel cmdUsysUniverseDel = new CmdUsysUniverseDel();
	public CmdUsysUniverseClear cmdUsysUniverseClear = new CmdUsysUniverseClear();
	
	public CmdUsysUniverse()
	{
		super();
		this.addAliases("u", "universe");
		
		this.addSubCommand(this.cmdUsysUniverseNew);
		this.addSubCommand(this.cmdUsysUniverseDel);
		this.addSubCommand(this.cmdUsysUniverseClear);
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_UNIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
