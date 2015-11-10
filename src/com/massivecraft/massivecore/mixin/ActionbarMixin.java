package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;

public interface ActionbarMixin
{
	// Abstract
	public boolean sendActionbarMessage(Object watcherObject, String message);

	// Parsed
	public boolean sendActionbarMsg(Object watcherObject, String message);

	// Mson
	public boolean sendActionbarMson(Object watcherObject, Mson mson);

	// Available
	public boolean isActionbarAvailable();

}
