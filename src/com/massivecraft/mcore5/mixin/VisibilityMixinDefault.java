package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.util.SenderUtil;

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
	public boolean isVisible(String watcherId, String watcheeId)
	{
		return this.isVisible(SenderUtil.getSender(watcherId), SenderUtil.getSender(watcheeId));
	}
	
	@Override
	public boolean isVisible(CommandSender watcher, String watcheeId)
	{
		return this.isVisible(watcher, SenderUtil.getSender(watcheeId));
	}
	
	@Override
	public boolean isVisible(String watcherId, CommandSender watchee)
	{
		return this.isVisible(SenderUtil.getSender(watcherId), watchee);
	}
	
	@Override
	public boolean isVisible(CommandSender watcher, CommandSender watchee)
	{
		Player pwatcher = SenderUtil.getAsPlayer(watcher);
		Player pwatchee = SenderUtil.getAsPlayer(watchee);
		
		if (pwatcher == null) return true;
		if (pwatchee == null) return true;
		
		return pwatcher.canSee(pwatchee);
	}
	
	@Override
	public void setVisible(String watcherId, String watcheeId, boolean visible)
	{
		this.setVisible(SenderUtil.getSender(watcherId), SenderUtil.getSender(watcheeId), visible);
	}
	
	@Override
	public void setVisible(CommandSender watcher, String watcheeId, boolean visible)
	{
		this.setVisible(watcher, SenderUtil.getSender(watcheeId), visible);
	}
	
	@Override
	public void setVisible(String watcherId, CommandSender watchee, boolean visible)
	{
		this.setVisible(SenderUtil.getSender(watcherId), watchee, visible);
	}
	
	@Override
	public void setVisible(CommandSender watcher, CommandSender watchee, boolean visible)
	{
		Player pwatcher = SenderUtil.getAsPlayer(watcher);
		Player pwatchee = SenderUtil.getAsPlayer(watchee);
		
		if (pwatcher == null) return;
		if (pwatchee == null) return;
		
		if (visible)
		{
			pwatcher.showPlayer(pwatchee);
		}
		else
		{
			pwatcher.hidePlayer(pwatchee);
		}
	}
}
