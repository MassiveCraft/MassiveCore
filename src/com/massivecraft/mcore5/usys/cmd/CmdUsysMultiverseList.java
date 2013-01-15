package com.massivecraft.mcore5.usys.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.arg.ARInteger;
import com.massivecraft.mcore5.cmd.req.ReqHasPerm;
import com.massivecraft.mcore5.usys.Multiverse;
import com.massivecraft.mcore5.usys.MultiverseColl;
import com.massivecraft.mcore5.util.Txt;

public class CmdUsysMultiverseList extends UsysCommand
{
	public CmdUsysMultiverseList()
	{
		this.addAliases("l", "list");
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(Permission.CMD_USYS_MULTIVERSE_LIST.node));
	}
	
	@Override
	public void perform()
	{
		Integer pageHumanBased = this.arg(0, ARInteger.get(), 1);
		if (pageHumanBased == null) return;
		
		// Create Messages
		List<String> lines = new ArrayList<String>();
		
		for (Multiverse multiverse : MultiverseColl.i.getAll())
		{
			lines.add("<h>"+multiverse.getId()+" <i>has "+Txt.implodeCommaAndDot(multiverse.getUniverses(), "<aqua>%s", "<i>, ", " <i>and ", "<i>."));
		}
				
		// Send them
		lines = Txt.parseWrap(lines);
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Multiverse List", sender));
	}
}
