package com.massivecraft.massivecore.cmd.massivecore;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysAspectList extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectList()
	{
		// Aliases
		this.addAliases("l", "list");
		
		// Args
		this.addOptionalArg("page", "1");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_ASPECT_LIST.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		
		// Create Lines
		List<String> lines = new ArrayList<String>();
		
		for (Aspect aspect : AspectColl.get().getAllRegistered())
		{
			String line = Txt.parse("<h>"+aspect.getId()+" <white>--> <h>"+aspect.getMultiverse().getId());
			lines.add(line);
		}
				
		// Send them
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Aspect List", sender));	
	}
	
}
