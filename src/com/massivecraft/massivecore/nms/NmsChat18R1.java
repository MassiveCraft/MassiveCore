package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class NmsChat18R1 extends NmsChatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsChat18R1 i = new NmsChat18R1();
	public static NmsChat18R1 get() { return i; }
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classChatSerializer = PackageType.MINECRAFT_SERVER.getClass("ChatSerializer");
		this.methodChatSerializer = ReflectionUtil.getMethod(this.classChatSerializer, "a", String.class);
		this.classEnumTitleAction = PackageType.MINECRAFT_SERVER.getClass("EnumTitleAction");
		
		setupCommon();
		
		this.constructorPacketPlayOutChatType = ReflectionUtil.getConstructor(this.classPacketPlayOutChat, this.classIChatBaseComponent, Byte.TYPE);
	}
	
}
