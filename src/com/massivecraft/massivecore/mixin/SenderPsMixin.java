package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.ps.PS;

public interface SenderPsMixin
{
	public PS getSenderPs(Object senderObject);
	public void setSenderPs(Object senderObject, PS ps);
}
