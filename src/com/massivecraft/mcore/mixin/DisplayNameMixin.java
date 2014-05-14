package com.massivecraft.mcore.mixin;

public interface DisplayNameMixin
{
	public String getDisplayName(Object senderObject);
	public void setDisplayName(Object senderObject, String displayName);
}
