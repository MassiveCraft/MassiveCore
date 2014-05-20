package com.massivecraft.mcore.mcorecmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.MCorePerm;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.VisibilityMode;
import com.massivecraft.mcore.cmd.arg.AREnum;
import com.massivecraft.mcore.cmd.arg.ARFloat;
import com.massivecraft.mcore.cmd.arg.ARInteger;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.particleeffect.ParticleEffect;

public class CmdMCoreTest extends MCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMCoreTest()
	{
		// Aliases
		this.addAliases("test");
		
		// Arg
		this.addRequiredArg("particleEffect");
		this.addRequiredArg("offsetX");
		this.addRequiredArg("offsetY");
		this.addRequiredArg("offsetZ");
		this.addRequiredArg("speed");
		this.addRequiredArg("amount");
		
		// Requirements
		this.addRequirements(ReqHasPerm.get(MCorePerm.TEST.node));
		this.addRequirements(ReqIsPlayer.get());
		
		// VisibilityMode
		this.setVisibilityMode(VisibilityMode.SECRET);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		ParticleEffect particleEffect = this.arg(0, AREnum.get(ParticleEffect.class));
		if (particleEffect == null) return;
		
		Location center = me.getEyeLocation().add(0, 0, 0);
		
		Float offsetX = this.arg(1, ARFloat.get());
		if (offsetX == null) return;
		
		Float offsetY = this.arg(2, ARFloat.get());
		if (offsetY == null) return;
		
		Float offsetZ = this.arg(3, ARFloat.get());
		if (offsetZ == null) return;
		
		Float speed = this.arg(4, ARFloat.get());
		if (speed == null) return;
		
		Integer amount = this.arg(5, ARInteger.get());
		if (amount == null) return;
		
		Player player = me;
		
		particleEffect.display(center, offsetX, offsetY, offsetZ, speed, amount, player);
	}
	
}
