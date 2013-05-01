package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.ExamineThread;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreMStoreStats extends MCoreCommand
{
	public CmdMCoreMStoreStats(List<String> aliases)
	{
		super(aliases);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE_STATS.node));
	}
	
	@Override
	public void perform()
	{
		msg(Txt.titleize("MStore Statistics"));
		msg("<k>Last Examine Duration: <v>%d<i>ms", ExamineThread.get().getLastDurationMillis());
	}
	
}
