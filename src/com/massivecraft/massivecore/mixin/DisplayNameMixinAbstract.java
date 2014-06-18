package com.massivecraft.massivecore.mixin;

public abstract class DisplayNameMixinAbstract implements DisplayNameMixin
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getDisplayName(Object senderObject)
	{
		return this.getDisplayName(senderObject, null);
	}
	
}