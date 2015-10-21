package com.massivecraft.massivecore.cmd.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.MultiverseColl;
import com.massivecraft.massivecore.cmd.Parameter;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysMultiverseList extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseList()
	{
		// Aliases
		this.addAliases("l", "list");
		
		// Parameters
		this.addParameter(Parameter.getPage());
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		int page = this.readArg();
		
		// Pager Create
		Pager<Multiverse> pager = new Pager<Multiverse>(this, "Multiverse List", page, MultiverseColl.get().getAll(), new Stringifier<Multiverse>()
		{
			@Override
			public String toString(Multiverse multiverse, int index)
			{
				return Txt.parse("<h>"+multiverse.getId()+" <i>has "+Txt.implodeCommaAndDot(multiverse.getUniverses(), "<aqua>%s", "<i>, ", " <i>and ", "<i>."));
			}
		}); 
		
		// Pager Message
		pager.message();
	}
	
}
