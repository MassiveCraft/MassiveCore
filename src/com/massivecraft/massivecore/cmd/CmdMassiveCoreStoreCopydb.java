package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Db;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.xlib.gson.JsonObject;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public class CmdMassiveCoreStoreCopydb extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreCopydb()
	{
		// Parameters
		this.addParameter(TypeString.get(), "from").setDesc("the database to copy from");
		this.addParameter(TypeString.get(), "to").setDesc("the database to copy to");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void perform() throws MassiveException
	{
		// Args
		final String fromAlias = this.readArg();
		final Db fromDb = MStore.getDb(fromAlias);
		if (fromDb == null)
		{
			msg("<b>could not get the from-database.");
			return;
		}
		
		final String toAlias = this.readArg();
		final Db toDb = MStore.getDb(toAlias);
		if (toDb == null)
		{
			msg("<b>could not get the to-database.");
			return;
		}
		
		// Prepare
		Set<String> collnames = fromDb.getCollnames();
		
		// Statistics
		int countCollCurrent = 0;
		int countCollTotal = collnames.size();
		
		// Do it!
		long before = System.currentTimeMillis();
		msg("<i>Now copying database with <h>%d <i>collections.", countCollTotal);
		for (String collname : fromDb.getCollnames())
		{
			countCollCurrent++;
			final Coll fromColl = new Coll(collname, Entity.class, fromDb, MassiveCore.get());
			final Coll toColl = new Coll(collname, Entity.class, toDb, MassiveCore.get());
			
			Collection<String> ids = fromDb.getIds(fromColl);
			msg("<i>Now copying collection <h>%d/%d %s <i>with <h>%d <i>documents.", countCollCurrent, countCollTotal, collname, ids.size());
			
			// Do a load check to verify we have access to this folder.
			if (ids.size() > 0 && fromDb.load(fromColl, ids.iterator().next()) == null)
			{
				msg("<b>Skipping <h>%s <b>since could not load data.", collname);
				continue;
			}
			
			for (String id : ids)
			{
				Entry<JsonObject, Long> data = fromDb.load(fromColl, id);
				toDb.save(toColl, id, data.getKey());
			}
		}
		long after = System.currentTimeMillis();
		long duration = after - before;
		msg("<g>The copy is now complete. <i>It took <h>%dms<i>.", duration);
	}
	
}
