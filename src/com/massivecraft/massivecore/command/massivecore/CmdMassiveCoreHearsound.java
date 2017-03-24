package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.combined.TypeSoundEffect;
import com.massivecraft.massivecore.command.type.container.TypeList;

import java.util.List;

public class CmdMassiveCoreHearsound extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreHearsound()
	{
		// Aliases
		this.addAliases("hearsound", "hearsounds");
		
		// Parameters
		this.addParameter(TypeList.get(TypeSoundEffect.get()), "sound(s)", true).setDesc("the sounds to hear");
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(MassiveCorePerm.HEARSOUND));
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException
	{
		// Args
		List<SoundEffect> soundEffects = this.readArg();
		
		// Apply
		SoundEffect.runAll(soundEffects, me);
	}
	
}
