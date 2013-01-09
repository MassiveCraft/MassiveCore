package com.massivecraft.mcore5.usys.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.arg.ARInteger;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.usys.Aspect;
import com.massivecraft.mcore5.usys.AspectColl;
import com.massivecraft.mcore5.util.Txt;

public class CmdUsysAspectList extends UsysCommand
{
	public CmdUsysAspectList()
	{
		this.addAliases("l", "list");
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_ASPECT_LIST.node));
	}
	
	@Override
	public void perform()
	{
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Messages
		List<String> lines = new ArrayList<String>();
		
		for (Aspect aspect : AspectColl.i.getAllRegistered())
		{
			lines.add("<h>"+aspect.getId()+" <white>--> <h>"+aspect.multiverse().getId());
		}
				
		// Send them
		lines = Txt.parseWrap(lines);
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Aspect List", sender));	
	}
}
