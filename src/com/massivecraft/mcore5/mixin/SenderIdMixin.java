package com.massivecraft.mcore5.mixin;

import java.util.Set;

public interface SenderIdMixin
{
	// Q: What do you mean "fix" a senderId?
	// A: A senderId is case insensitive. However a certain caseing may be the best looking one. Player do for example have an optimal caseing.
	public String reqFix(String senderId);
	public String tryFix(String senderId);
	public boolean canFix(String senderId);
	
	public boolean isOnline(String senderId);
	public boolean isOffline(String senderId);
	public boolean hasBeenOnline(String senderId);
	public Long getLastOnline(String senderId);
	
	// All of these are static snapshots that are alterable and won't change over time.
	public Set<String> getAllSenderIds();
	public Set<String> getOnlineSenderIds();
	public Set<String> getOfflineSenderIds();
	
}
