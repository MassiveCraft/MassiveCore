package com.massivecraft.mcore.store;

import java.util.Collection;

public interface SenderIdSource
{
	public Collection<Collection<String>> getSenderIdCollections();
}
