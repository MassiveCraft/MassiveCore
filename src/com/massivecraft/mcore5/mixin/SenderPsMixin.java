package com.massivecraft.mcore5.mixin;

import com.massivecraft.mcore5.PS;

public interface SenderPsMixin
{
	public PS getSenderPs(String senderId);
	public void setSenderPs(String senderId, PS ps);
}
