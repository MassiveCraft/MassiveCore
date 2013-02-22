package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.massivecraft.mcore.mixin.Mixin;

public class SenderIdSourceMixinAllPlayerIds implements SenderIdSource
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SenderIdSourceMixinAllPlayerIds i = new SenderIdSourceMixinAllPlayerIds();
	public static SenderIdSourceMixinAllPlayerIds get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<Collection<String>> getSenderIdCollections()
	{
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(Mixin.getAllPlayerIds());
		return ret;
	}
	
}
