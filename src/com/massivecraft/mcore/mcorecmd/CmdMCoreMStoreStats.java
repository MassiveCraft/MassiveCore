package com.massivecraft.mcore.mcorecmd;

import java.util.List;
import java.util.Map.Entry;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARColl;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.ExamineThread;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreMStoreStats extends MCoreCommand
{
	public CmdMCoreMStoreStats(List<String> aliases)
	{
		super(aliases);
		
		this.addOptionalArg("coll", Coll.TOTAL);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE_STATS.node));
	}
	
	@Override
	public void perform()
	{
		if (!this.argIsSet(0) || this.arg(0).equalsIgnoreCase(Coll.TOTAL))
		{
			this.performTotal();
		}
		else
		{
			Coll<?> coll = this.arg(0, ARColl.get());
			if (coll == null) return;
			this.performColl(coll);
		}
	}
	
	public void performTotal()
	{
		msg(Txt.titleize("MStore Total Statistics"));
		msg("<k>Last Examine Duration: <v>%d<i>ms", ExamineThread.get().getLastDurationMillis());
		msg("<a>== <k>Coll <a>| <k>Sync Count In <a>| <k>Sync Count Out <a>==");
		for (Entry<String, Coll<?>> entry : Coll.getMap().entrySet())
		{
			String name = entry.getKey();
			Coll<?> coll = entry.getValue();
			long in = coll.getSyncCount(Coll.TOTAL, true);
			long out = coll.getSyncCount(Coll.TOTAL, false);
			
			msg("<v>%s <a>| <v>%d <a>| <v>%d", name, in, out);
		}
	}
	
	public void performColl(Coll<?> coll)
	{
		msg(Txt.titleize("MStore "+coll.getName()+" Statistics"));
		msg("<k>Basename: <v>%s", coll.getBasename());
		msg("<k>Universe: <v>%s", coll.getUniverse());
		msg("<k>Entity Count: <v>%d", coll.getIds().size());
		msg("<k>Entity Class: <v>%s", coll.getEntityClass().getName());
		msg("<k>Plugin: <v>%s", coll.getPlugin().getDescription().getFullName());
		msg("<k>Database: <v>%s", coll.getDb().getName());
		msg("<k>Driver: <v>%s", coll.getDriver().getName());
		
		int limit;
		
		msg("<a>== Sync Count In <a>==");
		limit = 30;
		for (Entry<String, Long> entry : MUtil.entriesSortedByValues(coll.getSyncMap(true), false))
		{
			if (limit-- == 0) break;
			msg("<k>%s <v>%d", entry.getKey(), entry.getValue());
		}
		
		msg("<a>== Sync Count Out <a>==");
		limit = 30;
		for (Entry<String, Long> entry : MUtil.entriesSortedByValues(coll.getSyncMap(false), false))
		{
			if (limit-- == 0) break;
			msg("<k>%s <v>%d", entry.getKey(), entry.getValue());
		}
	}
}
