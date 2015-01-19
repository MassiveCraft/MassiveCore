package com.massivecraft.massivecore.mixin;

public interface KickMixin
{
	public boolean kick(Object senderObject);
	public boolean kick(Object senderObject, String message);
}
