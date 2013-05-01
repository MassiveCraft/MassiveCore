package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.HelpCommand;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.MUtil;

public class CmdMCoreUsys extends MCoreCommand
{
	public CmdMCoreUsysMultiverse cmdUsysMultiverse = new CmdMCoreUsysMultiverse(MUtil.list("m", "multiverse"));
	public CmdMCoreUsysUniverse cmdUsysUniverse = new CmdMCoreUsysUniverse(MUtil.list("u", "universe"));
	public CmdMCoreUsysWorld cmdUsysWorld = new CmdMCoreUsysWorld(MUtil.list("w", "world"));
	public CmdMCoreUsysAspect cmdUsysAspect = new CmdMCoreUsysAspect(MUtil.list("a", "aspect"));
	
	public CmdMCoreUsys(List<String> aliases)
	{
		super(aliases);
		
		this.addSubCommand(this.cmdUsysMultiverse);
		this.addSubCommand(this.cmdUsysUniverse);
		this.addSubCommand(this.cmdUsysWorld);
		this.addSubCommand(this.cmdUsysAspect);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS.node));
	}
	
	@Override
	public void perform()
	{
		this.getCommandChain().add(this);
		HelpCommand.getInstance().execute(this.sender, this.args, this.commandChain);
	}
}