package com.massivecraft.massivecore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.combined.TypeSoundEffect;
import com.massivecraft.massivecore.command.type.container.TypeList;

import java.util.List;

public class CmdMassiveCoreHearsound extends MassiveCoreCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreHearsound()
	{
		// Aliases
		this.addAliases("hearsounds");
		
		// Parameters
		this.addParameter(TypeList.get(TypeSoundEffect.get()), "sound(s)", true).setDesc("the sounds to hear");
		
		// Requirements
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
