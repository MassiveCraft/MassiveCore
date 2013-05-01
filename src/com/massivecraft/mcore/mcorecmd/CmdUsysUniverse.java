package com.massivecraft.mcore.mcorecmd;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;

public class CmdUsysUniverse extends MCommand
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
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_USYS_UNIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
