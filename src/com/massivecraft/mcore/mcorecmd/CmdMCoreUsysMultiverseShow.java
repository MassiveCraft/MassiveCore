package com.massivecraft.mcore.mcorecmd;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.mcore.Aspect;
import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.Multiverse;
import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.arg.ARMultiverse;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.util.Txt;

public class CmdMCoreUsysMultiverseShow extends MCoreCommand
{
	public CmdMCoreUsysMultiverseShow(List<String> aliases)
	{
		super(aliases);
		this.addRequiredArg("multiverse");
		
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_USYS_MULTIVERSE_SHOW.node));
	}
	
	@Override
	public void perform()
	{
		Multiverse multiverse = this.arg(0, ARMultiverse.get());
		if (multiverse == null) return;
		
		msg(Txt.titleize("Multiverse: "+multiverse.getId()));
		
		for (String universe : multiverse.getUniverses())
		{
			if (universe.equals(MCore.DEFAULT)) continue;
			msg("<aqua>"+universe+"<i>: "+Txt.implodeCommaAndDot(multiverse.getWorlds(universe), "<h>%s", "<i>, ", " <i>and ", "<i>."));
		}
		msg("<aqua>default<i>: for all other worlds.");
		
		msg("");
		msg("<i>Aspects using this multiverse:");
		this.msgAspects(multiverse.myAspects());
		
		msg("");
		msg("<i>Aspects NOT using this multiverse:");
		this.msgAspects(multiverse.otherAspects());
	}
	
	public void msgAspects(List<Aspect> aspects)
	{
		List<String> ids = new ArrayList<String>();
		for (Aspect aspect : aspects)
		{
			ids.add(aspect.getId());
		}
		
		if (ids.size() == 0)
		{
			msg("<i>*none*");
		}
		else
		{
			msg(Txt.implodeCommaAndDot(ids, "<h>%s", "<i>, ", " <i>and ", "<i>."));
		}
	}
}
