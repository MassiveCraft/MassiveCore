package com.massivecraft.massivecore.cmd.massivecore;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.VisibilityMode;
import com.massivecraft.massivecore.cmd.arg.AREnum;
import com.massivecraft.massivecore.cmd.arg.ARFloat;
import com.massivecraft.massivecore.cmd.arg.ARInteger;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.particleeffect.ParticleEffect;

public class CmdMassiveCoreTest extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreTest()
	{
		// Aliases
		this.addAliases("test");
		
		// Arg
		this.addArg(AREnum.get(ParticleEffect.class), "particleEffect").setDesc("the particle effect type to show");
		this.addArg(ARFloat.get(), "offsetX").setDesc("the maximum offset on x-axis for this particle");
		this.addArg(ARFloat.get(), "offsetY").setDesc("the maximum offset on y-axis for this particle");
		this.addArg(ARFloat.get(), "offsetZ").setDesc("the maximum offset on z-axis for this particle");
		this.addArg(ARFloat.get(), "speed").setDesc("the speed for this particle");
		this.addArg(ARInteger.get(), "amount").setDesc("the amount of particles to show");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.TEST.node));
		this.addRequirements(ReqIsPlayer.get());
		
		// VisibilityMode
		this.setVisibilityMode(VisibilityMode.SECRET);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Args
		ParticleEffect particleEffect = (ParticleEffect) this.readArg();
		
		Location center = me.getEyeLocation().add(0, 0, 0);
		
		float offsetX = (Float) this.readArg();
		float offsetY = (Float) this.readArg();
		float offsetZ = (Float) this.readArg();
		float speed = (Float) this.readArg();
		int amount = (Integer) this.readArg();
		
		Player player = me;
		
		particleEffect.display(center, offsetX, offsetY, offsetZ, speed, amount, player);
	}
	
}
