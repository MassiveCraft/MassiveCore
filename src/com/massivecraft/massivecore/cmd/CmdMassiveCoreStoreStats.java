package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.store.TypeColl;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

import java.util.Map.Entry;

public class CmdMassiveCoreStoreStats extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreStats()
	{
		// Parameters
		this.addParameter(TypeColl.get(), "coll", Coll.TOTAL).setDesc("the coll to show info about");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if ( ! this.argIsSet(0) || this.argAt(0).equalsIgnoreCase(Coll.TOTAL))
		{
			this.performTotal();
		}
		else
		{
			Coll<?> coll = this.readArg();
			this.performColl(coll);
		}
	}
	
	public void performTotal()
	{
		message(Txt.titleize("MStore Total Statistics"));
		//msg("<k>Last Examine Duration: <v>%d<i>ms", ExamineThread.get().getLastDurationMillis());
		msg("<a>== <k>Coll <a>| <k>Sync Count In <a>| <k>Sync Count Out <a>==");
		for (Entry<String, Coll<?>> entry : Coll.getMap().entrySet())
		{
			String name = entry.getKey();
			Coll<?> coll = entry.getValue();
			long in = coll.getSyncCountFixed(Coll.TOTAL, true);
			long out = coll.getSyncCountFixed(Coll.TOTAL, false);
			
			msg("<v>%s <a>| <v>%d <a>| <v>%d", name, in, out);
		}
	}
	
	public void performColl(Coll<?> coll)
	{
		message(Txt.titleize("MStore "+coll.getName()+" Statistics"));
		msg("<k>Basename: <v>%s", coll.getBasename());
		msg("<k>Universe: <v>%s", coll.getUniverse());
		msg("<k>Entity Count: <v>%d", coll.getIds().size());
		msg("<k>Entity Class: <v>%s", coll.getEntityClass().getName());
		msg("<k>Plugin: <v>%s", coll.getPlugin().getDescription().getFullName());
		msg("<k>Database: <v>%s", coll.getDb().getDbName());
		msg("<k>Driver: <v>%s", coll.getDb().getDriverName());
		
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
