package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;

public abstract class DisplayNameMixinAbstract implements DisplayNameMixin
{
	public Mson getDisplayNameMson(Object senderObject, Object watcherObject)
	{
		String displayName = this.getDisplayName(senderObject, watcherObject);
		if (displayName == null) return null;
		return Mson.fromParsedMessage(displayName);
	}
}
