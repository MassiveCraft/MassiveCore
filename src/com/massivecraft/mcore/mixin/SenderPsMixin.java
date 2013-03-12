package com.massivecraft.mcore.mixin;

import com.massivecraft.mcore.ps.PS;

public interface SenderPsMixin
{
	public PS getSenderPs(String senderId);
	public void setSenderPs(String senderId, PS ps);
}
