package com.massivecraft.massivecore.store;

import java.util.Collection;

public interface SenderIdSource
{
	public Collection<Collection<String>> getSenderIdCollections();
}
