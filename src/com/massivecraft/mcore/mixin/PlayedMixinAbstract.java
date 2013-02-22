package com.massivecraft.mcore.mixin;

public abstract class PlayedMixinAbstract implements PlayedMixin
{

	@Override
	public boolean isOffline(String senderId)
	{
		return !this.isOnline(senderId);
	}

	@Override
	public boolean hasPlayedBefore(String senderId)
	{
		Long firstPlayed = this.getFirstPlayed(senderId);
		return firstPlayed != null && firstPlayed != 0;
	}

}
