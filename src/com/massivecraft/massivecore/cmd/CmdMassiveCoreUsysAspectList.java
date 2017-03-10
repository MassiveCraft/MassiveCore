package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;

public class CmdMassiveCoreUsysAspectList extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysAspectList()
	{
		// Parameters
		this.addParameter(Parameter.getPage()).setDesc("the page in the aspect list");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		int page = this.readArg();
		
		// Pager Create
		Pager<Aspect> pager = new Pager<>(this, "Aspect List", page, AspectColl.get().getAllRegistered(), new Stringifier<Aspect>()
		{
			@Override
			public String toString(Aspect aspect, int index)
			{
				return Txt.parse("<h>" + aspect.getId() + " <white>--> <h>" + aspect.getMultiverse().getId());
			}
		}); 
		
		// Pager Message
		pager.message();
	}
	
}
