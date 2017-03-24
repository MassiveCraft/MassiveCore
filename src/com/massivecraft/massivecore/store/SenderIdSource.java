package com.massivecraft.massivecore.store;

import java.util.Collection;

public interface SenderIdSource
{
	Collection<Collection<String>> getSenderIdCollections();
}
