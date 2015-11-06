package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;

public interface ModificationMixin
{
	public void syncModification(Entity<?> entity);
	public void syncModification(Coll<?> coll, String id);
}
