package com.massivecraft.mcore.mixin;

public abstract class CommandMixinAbstract implements CommandMixin
{
	@Override
	public boolean dispatchCommand(Object senderObject, String commandLine)
	{
		return this.dispatchCommand(senderObject, senderObject, commandLine);
	}
}
