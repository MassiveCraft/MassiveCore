package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.Coll;
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
		msg("<a>== <k>Coll <a>| <k>Sync Count In <a>| <k>Sync Count Out <a>==");
		for (String name : Coll.getNames())
		{
			long in = Coll.getSyncCount(name, true);
			long out = Coll.getSyncCount(name, false);
			msg("<v>%s <a>| <v>%d <a>| <v>%d", name, in, out);
		}
	}
	
}
