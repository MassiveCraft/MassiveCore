package com.massivecraft.mcore.mixin;

import com.massivecraft.mcore.ps.PS;

public interface SenderPsMixin
{
	public PS getSenderPs(Object senderObject);
	public void setSenderPs(Object senderObject, PS ps);
}
