package com.massivecraft.massivecore.cmd.massivecore;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.MultiverseColl;
import com.massivecraft.massivecore.cmd.ArgSetting;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
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
		
		// Args
		this.addArg(ArgSetting.getPage());
		
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
		
		// Create Lines
		List<String> lines = new ArrayList<String>();
		for (Multiverse multiverse : MultiverseColl.get().getAll())
		{
			String line = Txt.parse("<h>"+multiverse.getId()+" <i>has "+Txt.implodeCommaAndDot(multiverse.getUniverses(), "<aqua>%s", "<i>, ", " <i>and ", "<i>."));
			lines.add(line);
		}
				
		// Send them
		this.sendMessage(Txt.getPage(lines, page, "Multiverse List", sender));
	}
	
}
