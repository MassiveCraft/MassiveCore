package com.massivecraft.mcore5.mixin;

import java.util.Set;

public interface SenderIdMixin
{
	// Q: What do you mean "fix" a senderId?
	// A: A senderId is case insensitive. However a certain caseing may be the best looking one. Player do for example have an optimal caseing.
	public String reqFix(String senderId);
	public String tryFix(String senderId);
	public boolean canFix(String senderId);
	
	// These may be unmodifiable. Don't expect them to be changeable.
	public Set<String> getAllSenderIds();
	public Set<String> getOnlineSenderIds();
	public Set<String> getOfflineSenderIds();
	
	public Set<String> getAllPlayerIds();
	public Set<String> getOnlinePlayerIds();
	public Set<String> getOfflinePlayerIds();
	
}
