package com.massivecraft.mcore.mcorecmd;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.massivecraft.mcore.ConfServer;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.NaturalOrderComparator;
import com.massivecraft.mcore.cmd.arg.ARString;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.Db;
import com.massivecraft.mcore.store.MStore;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreMStoreListcolls extends MCoreCommand
{
	public CmdMCoreMStoreListcolls(List<String> aliases)
	{
		super(aliases);
		
		this.addOptionalArg("db", ConfServer.dburi);
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_MSTORE_LISTCOLLS.node));
	}
	
	@Override
	public void perform()
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
