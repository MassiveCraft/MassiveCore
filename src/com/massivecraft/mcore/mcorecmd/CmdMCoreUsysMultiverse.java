package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCoreUsysMultiverse extends MCoreCommand
{
	public CmdMCoreUsysMultiverseList cmdUsysMultiverseList = new CmdMCoreUsysMultiverseList(MUtil.list("l", "list"));
	public CmdMCoreUsysMultiverseShow cmdUsysMultiverseShow = new CmdMCoreUsysMultiverseShow(MUtil.list("s", "show"));
	public CmdMCoreUsysMultiverseNew cmdUsysMultiverseNew = new CmdMCoreUsysMultiverseNew(MUtil.list("n", "new"));
	public CmdMCoreUsysMultiverseDel cmdUsysMultiverseDel = new CmdMCoreUsysMultiverseDel(MUtil.list("d", "del"));
	
	public CmdMCoreUsysMultiverse(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdUsysMultiverseList);
		this.addSubCommand(this.cmdUsysMultiverseShow);
		this.addSubCommand(this.cmdUsysMultiverseNew);
		this.addSubCommand(this.cmdUsysMultiverseDel);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}
