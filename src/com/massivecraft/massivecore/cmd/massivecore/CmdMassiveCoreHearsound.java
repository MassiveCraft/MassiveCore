package com.massivecraft.massivecore.cmd.massivecore;

import java.util.List;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.cmd.type.TypeList;
import com.massivecraft.massivecore.cmd.type.TypeSoundEffect;

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
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.HEARSOUND.node));
		this.addRequirements(ReqIsPlayer.get());
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
