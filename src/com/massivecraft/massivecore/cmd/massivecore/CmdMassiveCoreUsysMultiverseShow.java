package com.massivecraft.massivecore.cmd.massivecore;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARMultiverse;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysMultiverseShow extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseShow()
	{
		// Aliases
		this.addAliases("s", "show");
		
		// Args
		this.addRequiredArg("multiverse");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE_SHOW.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse multiverse = this.arg(0, ARMultiverse.get());
		
		msg(Txt.titleize("Multiverse: "+multiverse.getId()));
		
		for (String universe : multiverse.getUniverses())
		{
			if (universe.equals(MassiveCore.DEFAULT)) continue;
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
