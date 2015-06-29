package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;

public interface DisplayNameMixin
{
	public Mson getDisplayNameMson(Object senderObject, Object watcherObject);
	public String getDisplayName(Object senderObject, Object watcherObject);
}
