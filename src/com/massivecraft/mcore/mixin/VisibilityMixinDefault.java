package com.massivecraft.mcore.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore.util.SenderUtil;

public class VisibilityMixinDefault extends VisibilityMixinAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static VisibilityMixinDefault i = new VisibilityMixinDefault();
	public static VisibilityMixinDefault get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean canSee(String watcherId, String watcheeId)
	{
		return this.canSee(SenderUtil.getSender(watcherId), SenderUtil.getSender(watcheeId));
	}
	
	@Override
	public boolean canSee(CommandSender watcher, String watcheeId)
	{
		return this.canSee(watcher, SenderUtil.getSender(watcheeId));
	}
	
	@Override
	public boolean canSee(String watcherId, CommandSender watchee)
	{
		return this.canSee(SenderUtil.getSender(watcherId), watchee);
	}
	
	@Override
	public boolean canSee(CommandSender watcher, CommandSender watchee)
	{
		Player pwatcher = SenderUtil.getAsPlayer(watcher);
		Player pwatchee = SenderUtil.getAsPlayer(watchee);
		
		if (pwatcher == null) return true;
		if (pwatchee == null) return true;
		
		return pwatcher.canSee(pwatchee);
	}
	
}
