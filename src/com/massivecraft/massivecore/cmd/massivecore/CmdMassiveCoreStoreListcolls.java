package com.massivecraft.massivecore.cmd.massivecore;

import java.util.Set;
import java.util.TreeSet;

import com.massivecraft.massivecore.ConfServer;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.NaturalOrderComparator;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARString;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Db;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreStoreListcolls extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreStoreListcolls()
	{
		// Aliases
		this.addAliases("listcolls");
		
		// Args
		this.addOptionalArg("db", ConfServer.dburi);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.STORE_LISTCOLLS.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		final String dbAlias = this.arg(0, ARString.get(), ConfServer.dburi);
		final Db db = MStore.getDb(dbAlias);
		if (db == null)
		{
			msg("<b>could not get the database.");
			return;
		}
		
		// Prepare
		Set<String> collnames = new TreeSet<String>(NaturalOrderComparator.get());
		collnames.addAll(db.getCollnames());
		
		// Do it!
		msg(Txt.titleize("Collections in "+db.getName()));
		for (String collname : collnames)
		{
			String message = Txt.parse("<h>") + collname;
			
			Coll<?> coll = null;
			
			for (Coll<?> collCandidate : Coll.getInstances())
			{
				if (!collCandidate.getName().equals(collname)) continue;
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
			
			sendMessage(message);
		}
	}
	
}
