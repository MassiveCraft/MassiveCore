package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.mson.Mson;

public abstract class DisplayNameMixinAbstract implements DisplayNameMixin
{
	public Mson getDisplayNameMson(Object senderObject, Object watcherObject)
	{
		return Mson.fromParsedMessage(this.getDisplayName(senderObject, watcherObject));
	}
}
