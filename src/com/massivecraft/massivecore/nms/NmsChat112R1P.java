package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.entity.Player;

public class NmsChat112R1P extends NmsChatAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsChat112R1P i = new NmsChat112R1P();
	public static NmsChat112R1P get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	protected Class<?> classChatMessageType;
	protected Object instanceChatMessageTypeGameInfo;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		this.classChatSerializer = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer");
		this.methodChatSerializer = ReflectionUtil.getMethod(this.classChatSerializer, "a", String.class);
		this.classEnumTitleAction = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle$EnumTitleAction");
		
		setupCommon();
		
		this.classChatMessageType = PackageType.MINECRAFT_SERVER.getClass("ChatMessageType");
		this.instanceChatMessageTypeGameInfo = this.classChatMessageType.getEnumConstants()[2];
		// CHAT(0),
		// SYSTEM(1),
		// GAME_INFO(2);
		this.constructorPacketPlayOutChatType = ReflectionUtil.getConstructor(this.classPacketPlayOutChat, this.classIChatBaseComponent, this.classChatMessageType);
	}
	
	// -------------------------------------------- //
	// ACTIONBAR
	// -------------------------------------------- //
	
	@Override
	public <T> T constructActionBarPacket(Object component) {
		return ReflectionUtil.invokeConstructor(this.constructorPacketPlayOutChatType, component, this.instanceChatMessageTypeGameInfo);
	}
	
}
