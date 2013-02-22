package com.massivecraft.mcore.mixin;

public abstract class SenderIdMixinAbstract implements SenderIdMixin
{
	@Override
	public String tryFix(String senderId)
	{
		String ret = this.reqFix(senderId);
		if (ret != null) return ret;
		return senderId;
	}

	@Override
	public boolean canFix(String senderId)
	{
		return this.reqFix(senderId) != null;
	}
}