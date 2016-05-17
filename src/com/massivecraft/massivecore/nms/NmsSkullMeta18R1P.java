package com.massivecraft.massivecore.nms;

public class NmsSkullMeta18R1P extends NmsSkullMetaAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsSkullMeta18R1P i = new NmsSkullMeta18R1P();
	public static NmsSkullMeta18R1P get () { return i; }
	
	// -------------------------------------------- //
	// GAME PROFILE CLASS NAME
	// -------------------------------------------- //
	
	public String getGameProfileClassName()
	{
		return "com.mojang.authlib.GameProfile";
	}
	
}
