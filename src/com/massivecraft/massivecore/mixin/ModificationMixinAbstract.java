package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.store.Entity;

public abstract class ModificationMixinAbstract implements ModificationMixin
{
	public void syncModification(Entity<?> entity)
	{
		this.syncModification(entity.getColl(), entity.getId());
	}
}
