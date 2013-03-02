package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.massivecraft.mcore.mixin.Mixin;

public class SenderIdSourceMixinOnlinePlayerIds implements SenderIdSource
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SenderIdSourceMixinOnlinePlayerIds i = new SenderIdSourceMixinOnlinePlayerIds();
	public static SenderIdSourceMixinOnlinePlayerIds get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<Collection<String>> getSenderIdCollections()
	{
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(Mixin.getOnlinePlayerIds());
		return ret;
	}
	
}
