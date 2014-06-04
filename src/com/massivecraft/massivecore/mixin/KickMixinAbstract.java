package com.massivecraft.massivecore.mixin;

public abstract class KickMixinAbstract implements KickMixin
{
	@Override
	public boolean kick(Object senderObject)
	{
		return this.kick(senderObject, null);
	}
}
