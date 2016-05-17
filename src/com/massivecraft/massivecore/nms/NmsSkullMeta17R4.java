package com.massivecraft.massivecore.nms;

public class NmsSkullMeta17R4 extends NmsSkullMetaAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsSkullMeta17R4 i = new NmsSkullMeta17R4();
	public static NmsSkullMeta17R4 get () { return i; }
	
	// -------------------------------------------- //
	// GAME PROFILE CLASS NAME
	// -------------------------------------------- //
	
	public String getGameProfileClassName()
	{
		return "net.minecraft.util.com.mojang.authlib.GameProfile";
	}
	
}
