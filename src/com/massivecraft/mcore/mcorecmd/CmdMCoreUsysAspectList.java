package com.massivecraft.mcore.mcorecmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.AspectColl;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreUsysAspectList extends MCoreCommand
{
	public CmdMCoreUsysAspectList(List<String> aliases)
	{
		super(aliases);
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_ASPECT_LIST.node));
	}
	
	@Override
	public void perform()
	{
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Messages
		List<String> lines = new ArrayList<String>();
		
		for (Aspect aspect : AspectColl.get().getAllRegistered())
		{
			lines.add("<h>"+aspect.getId()+" <white>--> <h>"+aspect.getMultiverse().getId());
		}
				
		// Send them
		lines = Txt.parseWrap(lines);
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Aspect List", sender));	
	}
}
