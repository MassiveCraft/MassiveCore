package com.massivecraft.mcore.mcorecmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MultiverseColl;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreUsysMultiverseList extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreUsysMultiverseList()
	{
		// Aliases
		this.addAliases("l", "list");
		
		// Args
		this.addOptionalArg("page", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Messages
		List<String> lines = new ArrayList<String>();
		
		for (Multiverse multiverse : MultiverseColl.get().getAll())
		{
			lines.add("<h>"+multiverse.getId()+" <i>has "+Txt.implodeCommaAndDot(multiverse.getUniverses(), "<aqua>%s", "<i>, ", " <i>and ", "<i>."));
		}
				
		// Send them
		lines = Txt.parseWrap(lines);
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Multiverse List", sender));
	}
	
}
