package com.massivecraft.mcore.mixin;

public interface CommandMixin
{
	public boolean dispatchCommand(Object senderObject, String commandLine);
	public boolean dispatchCommand(Object presentObject, Object senderObject, String commandLine); // This one is non-abstract
	
}
