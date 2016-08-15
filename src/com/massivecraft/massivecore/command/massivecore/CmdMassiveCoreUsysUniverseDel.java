package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.Multiverse;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.command.type.store.TypeMultiverse;

public class CmdMassiveCoreUsysUniverseDel extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreUsysUniverseDel()
	{
		// Aliases
		this.addAliases("del");
		
		// Parameters
		this.addParameter(TypeString.get(), "universe").setDesc("the universe to delete");
		this.addParameter(TypeMultiverse.get(), "multiverse").setDesc("the multiverse of the universe to delete");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.USYS_MULTIVERSE_DEL.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		String universe = this.readArg();
		Multiverse multiverse = this.readArg();
		
		if (universe.equals(MassiveCore.DEFAULT))
		{
			msg("<b>You can't remove the default universe.");
			msg("<b>Each multiverse contains a default universe.");
			return;
		}
		
		if (!multiverse.containsUniverse(universe))
		{
			msg("<b>No universe <h>%s<b> exists in multiverse <h>%s<b>.", universe, multiverse.getId());
			return;
		}
		
		multiverse.delUniverse(universe);
		
		msg("<g>Deleted universe <h>%s<g> in multiverse <h>%s<g>.", universe, multiverse.getId());
	}
	
}
