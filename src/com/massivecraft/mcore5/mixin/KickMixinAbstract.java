package com.massivecraft.mcore5.mixin;

import org.bukkit.command.CommandSender;

public abstract class KickMixinAbstract implements KickMixin
{
	@Override
	public boolean kick(CommandSender sender)
	{
		return this.kick(sender, null);
	}

	@Override
	public boolean kick(String senderId)
	{
		return this.kick(senderId, null);
	}
}
