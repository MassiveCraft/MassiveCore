package com.massivecraft.massivecore.mixin;

import com.massivecraft.massivecore.Log;

public class MixinLog extends Mixin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MixinLog d = new MixinLog();
	private static MixinLog i = d;
	public static MixinLog get() { return i; }
	
	// -------------------------------------------- //
	// SEND
	// -------------------------------------------- //
	
	public void send(Log log)
	{
		
	}

}
