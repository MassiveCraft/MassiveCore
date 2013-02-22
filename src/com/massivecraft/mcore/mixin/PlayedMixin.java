package com.massivecraft.mcore.mixin;

public interface PlayedMixin
{
	public boolean isOnline(String senderId);
	public boolean isOffline(String senderId);
	public Long getFirstPlayed(String senderId);
	public Long getLastPlayed(String senderId);
	public boolean hasPlayedBefore(String senderId);
}
