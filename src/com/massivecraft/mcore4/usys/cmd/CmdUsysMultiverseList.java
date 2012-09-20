package com.massivecraft.mcore4.usys.cmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore4.Permission;
import com.massivecraft.mcore4.cmd.arg.ARInteger;
import com.massivecraft.mcore4.cmd.req.ReqHasPerm;
import com.massivecraft.mcore4.usys.Multiverse;
import com.massivecraft.mcore4.usys.MultiverseColl;
import com.massivecraft.mcore4.util.Txt;

public class CmdUsysMultiverseList extends UsysCommand
{
	public CmdUsysMultiverseList()
	{
		this.addAliases("l", "list");
		this.addOptionalArg("page", "1");
		
		this.addRequirements(ReqHasPerm.get(Permission.USYS_MULTIVERSE_LIST.node));
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
			if (multiverse.getUniverses().size() > 0)
			{
				lines.add("<h>"+multiverse.getId()+" <i>has "+Txt.implodeCommaAndDot(multiverse.getUniverses(), "<aqua>%s", "<i>, ", " <i>and ", "<i>."));
			}
			else
			{
				lines.add("<h>"+multiverse.getId()+" <i>has no universes.");
			}
		}
				
		// Send them
		lines = Txt.parseWrap(lines);
		this.sendMessage(Txt.getPage(lines, pageHumanBased, "Multiverse List"));	
	}
}
