package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.store.TypeAspect;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysAspectShow extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectShow()
	{
		// Parameters
		this.addParameter(TypeAspect.get(), "aspect").setDesc("the aspect to show info about");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Aspect aspect = this.readArg();
		
		message(Txt.titleize("Aspect: "+aspect.getId()));
		msg("<k>using multiverse: <v>%s",aspect.getMultiverse().getId());
		
		for (String descLine : aspect.getDesc())
		{
			msg(descLine);
		}
	}
	
}
