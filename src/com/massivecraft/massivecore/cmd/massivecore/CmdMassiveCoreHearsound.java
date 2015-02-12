package com.massivecraft.massivecore.cmd.massivecore;

import java.util.List;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.arg.ARList;
import com.massivecraft.massivecore.cmd.arg.ARSoundEffect;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdMassiveCoreHearsound extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreHearsound()
	{
		// Aliases
		this.addAliases("hearsound", "hearsounds");
		
		// Args
		this.addRequiredArg("sound(s)");
		this.setErrorOnToManyArgs(false);
		
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
		List<SoundEffect> soundEffects = this.argConcatFrom(0, ARList.get(ARSoundEffect.get()));
		
		// Apply
		SoundEffect.runAll(soundEffects, me);
	}
	
}
