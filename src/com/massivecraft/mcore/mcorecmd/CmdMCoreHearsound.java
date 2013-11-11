package com.massivecraft.mcore.mcorecmd;

import java.util.List;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.SoundEffect;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.arg.ARSoundEffects;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdMCoreHearsound extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreHearsound()
	{
		// Aliases
		this.addAliases("hearsound", "hearsounds");
		
		// Args
		this.addRequiredArg("sound(s)");
		this.setErrorOnToManyArgs(false);
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.CMD_MCORE_HEARSOUND.node));
		this.addRequirements(ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		List<SoundEffect> soundEffects = this.argConcatFrom(0, ARSoundEffects.get());
		if (soundEffects == null) return;
		
		// Apply
		SoundEffect.runAll(soundEffects, me);
	}
	
}
