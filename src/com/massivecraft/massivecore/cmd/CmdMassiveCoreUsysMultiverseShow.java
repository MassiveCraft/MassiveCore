package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;
import com.massivecraft.massivecore.util.Txt;

import java.util.ArrayList;
import java.util.List;


public class CmdMassiveCoreUsysMultiverseShow extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysMultiverseShow()
	{
		// Parameters
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse to show info about");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Multiverse multiverse = this.readArg();
		
		message(Txt.titleize("Multiverse: "+multiverse.getId()));
		
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
		List<String> ids = new ArrayList<>();
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
