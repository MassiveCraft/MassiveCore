package com.massivecraft.mcore5.store;

import java.util.Collection;

import com.massivecraft.mcore5.mixin.Mixin;

public class MixinSenderIdSource implements SenderIdSource
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinSenderIdSource i = new MixinSenderIdSource();
	public static MixinSenderIdSource get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<String> getSenderIds()
	{
		return Mixin.getAllSenderIds();
	}
	
}
