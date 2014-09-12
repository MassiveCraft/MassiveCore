package com.massivecraft.massivecore.mixin;

public interface PlayedMixin
{
	public boolean isOnline(Object senderObject);
	public boolean isOffline(Object senderObject);
	public Long getFirstPlayed(Object senderObject);
	public Long getLastPlayed(Object senderObject);
	public boolean hasPlayedBefore(Object senderObject);
}
