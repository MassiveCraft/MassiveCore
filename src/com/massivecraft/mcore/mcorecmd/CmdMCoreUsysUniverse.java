package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCoreUsysUniverse extends MCoreCommand
{
	public CmdMCoreUsysUniverseNew cmdUsysUniverseNew = new CmdMCoreUsysUniverseNew(MUtil.list("n", "new"));
	public CmdMCoreUsysUniverseDel cmdUsysUniverseDel = new CmdMCoreUsysUniverseDel(MUtil.list("d", "del"));
	public CmdMCoreUsysUniverseClear cmdUsysUniverseClear = new CmdMCoreUsysUniverseClear(MUtil.list("c", "clear"));
	
	public CmdMCoreUsysUniverse(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdUsysUniverseNew);
		this.addSubCommand(this.cmdUsysUniverseDel);
		this.addSubCommand(this.cmdUsysUniverseClear);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_UNIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
