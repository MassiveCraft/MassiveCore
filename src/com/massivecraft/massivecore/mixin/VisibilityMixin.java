package com.massivecraft.massivecore.mixin;

public interface VisibilityMixin
{
	public boolean isVisible(Object watcheeObject);
	public boolean isVisible(Object watcheeObject, Object watcherObject);
}
