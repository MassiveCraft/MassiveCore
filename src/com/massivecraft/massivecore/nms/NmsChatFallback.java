package com.massivecraft.massivecore.nms;

public class NmsChatFallback extends NmsChat
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsChatFallback i = new NmsChatFallback();
	public static NmsChatFallback get() { return i; }
	
}
