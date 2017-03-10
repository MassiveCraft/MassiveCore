package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.ConfServer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.comparator.ComparatorNaturalOrder;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Db;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.util.Txt;

import java.util.Set;
import java.util.TreeSet;

public class CmdMassiveCoreStoreListcolls extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreListcolls()
	{
		// Parameters
		this.addParameter(TypeString.get(), "db", ConfServer.dburi).setDesc("the database to list colls from");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		final String dbAlias = this.readArg(ConfServer.dburi);
		final Db db = MStore.getDb(dbAlias);
		if (db == null)
		{
			msg("<b>could not get the database.");
			return;
		}
		
		// Prepare
		Set<String> collnames = new TreeSet<>(ComparatorNaturalOrder.get());
		collnames.addAll(db.getCollnames());
		
		// Do it!
		message(Txt.titleize("Collections in "+db.getDbName()));
		for (String collname : collnames)
		{
			String message = Txt.parse("<h>") + collname;
			
			Coll<?> coll = null;
			
			for (Coll<?> collCandidate : Coll.getInstances())
			{
				if ( ! collCandidate.getName().equals(collname)) continue;
				if (collCandidate.getDb() != db) continue;
				coll = collCandidate;
				break;
			}
			
			if (coll == null)
			{
				message += Txt.parse(" <b>UNUSED");
			}
			else
			{
				message += Txt.parse(" <i>(%d documents)", coll.getIds().size());
			}
			
			message(message);
		}
	}
	
}
