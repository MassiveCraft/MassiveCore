package com.massivecraft.mcore.mixin;

public abstract class KickMixinAbstract implements KickMixin
{
	@Override
	public boolean kick(Object senderObject)
	{
		return this.kick(senderObject, null);
	}
}
