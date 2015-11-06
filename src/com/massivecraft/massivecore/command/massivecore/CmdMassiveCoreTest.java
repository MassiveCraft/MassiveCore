package com.massivecraft.massivecore.command.massivecore;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;

public class CmdMassiveCoreTest extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMassiveCoreTest()
	{
		// Aliases
		this.addAliases("test");
		
		
		// OLD STUFF
		// Arg
		/*this.addArg(AREnum.get(ParticleEffect.class), "particleEffect").setDesc("the particle effect type to show");
		this.addArg(ARFloat.get(), "offsetX").setDesc("the maximum offset on x-axis for this particle");
		this.addArg(ARFloat.get(), "offsetY").setDesc("the maximum offset on y-axis for this particle");
		this.addArg(ARFloat.get(), "offsetZ").setDesc("the maximum offset on z-axis for this particle");
		this.addArg(ARFloat.get(), "speed").setDesc("the speed for this particle");
		this.addArg(ARInteger.get(), "amount").setDesc("the amount of particles to show");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MassiveCorePerm.TEST.node));
		this.addRequirements(ReqIsPlayer.get());
		
		// VisibilityMode
		this.setVisibilityMode(VisibilityMode.SECRET);*/
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		
		message(mson("This is your ", mson("item").item(me.getItemInHand())));
		// OLD STUFF
		/*// Args
		ParticleEffect particleEffect = this.readArg();
		
		Location center = me.getEyeLocation().add(0, 0, 0);
		
		float offsetX = this.readArg();
		float offsetY = this.readArg();
		float offsetZ = this.readArg();
		float speed = this.readArg();
		int amount = this.readArg();
		
		Player player = me;
		
		particleEffect.display(center, offsetX, offsetY, offsetZ, speed, amount, player);*/
	}
	
}
