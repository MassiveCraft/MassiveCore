package com.massivecraft.massivecore.store.cleanable;

import com.massivecraft.massivecore.store.Coll;

// This interface only really makes sense for SenderEntity's
// But by using this conversion to non-senders should be easier when that is done
public interface Cleanable
{
	boolean shouldBeCleaned(long now);
	Coll<?> getColl();
	
	void preClean();
	void postClean();
}
