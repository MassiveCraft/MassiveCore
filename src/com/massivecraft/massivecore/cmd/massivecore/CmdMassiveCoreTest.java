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
		this.addRequiredArg("particleEffect");
		this.addRequiredArg("offsetX");
		this.addRequiredArg("offsetY");
		this.addRequiredArg("offsetZ");
		this.addRequiredArg("speed");
		this.addRequiredArg("amount");
		
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
		ParticleEffect particleEffect = this.arg(0, AREnum.get(ParticleEffect.class));
		
		Location center = me.getEyeLocation().add(0, 0, 0);
		
		Float offsetX = this.arg(1, ARFloat.get());
		Float offsetY = this.arg(2, ARFloat.get());
		Float offsetZ = this.arg(3, ARFloat.get());
		Float speed = this.arg(4, ARFloat.get());
		Integer amount = this.arg(5, ARInteger.get());
		
		Player player = me;
		
		particleEffect.display(center, offsetX, offsetY, offsetZ, speed, amount, player);
	}
	
}
