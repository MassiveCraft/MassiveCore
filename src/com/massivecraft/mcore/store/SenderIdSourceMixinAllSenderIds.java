package com.massivecraft.mcore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.massivecraft.mcore.util.IdUtil;

public class SenderIdSourceMixinAllSenderIds implements SenderIdSource
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static SenderIdSourceMixinAllSenderIds i = new SenderIdSourceMixinAllSenderIds();
	public static SenderIdSourceMixinAllSenderIds get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Collection<Collection<String>> getSenderIdCollections()
	{
		List<Collection<String>> ret = new ArrayList<Collection<String>>();
		ret.add(IdUtil.getAllNames());
		ret.add(IdUtil.getAllIds());
		return ret;
	}
	
}
