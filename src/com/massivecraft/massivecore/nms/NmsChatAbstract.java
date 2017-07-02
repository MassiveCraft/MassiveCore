package com.massivecraft.massivecore.nms;

import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.ReflectionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class NmsChatAbstract extends NmsChat
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// EnumTitleAction
	protected Class<?> classEnumTitleAction;
	protected Enum<?> enumEnumTitleActionMain;
	protected Enum<?> enumEnumTitleActionSub;
	protected Enum<?> enumEnumTitleActionTimes;

	// ChatSerializer
	protected Class<?> classChatSerializer;
	protected Method methodChatSerializer;
	
	// IChatBaseComponent
	protected Class<?> classIChatBaseComponent;

	// PacketPlayOutTitle
	protected Class<?> classPacketPlayOutTitle;
	protected Constructor<?> constructorPacketPlayOutTitle;
	protected Constructor<?> constructorPacketPlayOutTitleTimes;

	// PacketPlayOutChat
	protected Class<?> classPacketPlayOutChat;
	protected Constructor<?> constructorPacketPlayOutChat;
	protected Constructor<?> constructorPacketPlayOutChatType;
	
	// -------------------------------------------- //
	// PROVOKE
	// -------------------------------------------- //
	
	@Override
	public Class<ArmorStand> provoke() throws Throwable
	{
		// Require NmsBasics
		NmsBasics.get().require();
				
		// Require 1.8
		return ArmorStand.class;
	}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	@Override
	public void setup() throws Throwable
	{
		throw notImplemented();
	}
	
	// -------------------------------------------- //
	// SETUP COMMON
	// -------------------------------------------- //
	
	protected void setupCommon() throws Throwable {
		for (Object object : this.classEnumTitleAction.getEnumConstants())
		{
			Enum<?> e = (Enum<?>) object;
			if (e.name().equalsIgnoreCase("TITLE")) this.enumEnumTitleActionMain = e;
			else if (e.name().equalsIgnoreCase("SUBTITLE")) this.enumEnumTitleActionSub = e;
			else if (e.name().equalsIgnoreCase("TIMES")) this.enumEnumTitleActionTimes = e;
		}
		
		this.classIChatBaseComponent = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent");
		
		// Get title packet and it's constructor
		this.classPacketPlayOutTitle = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle");
		this.constructorPacketPlayOutTitle = ReflectionUtil.getConstructor(this.classPacketPlayOutTitle, this.classEnumTitleAction, this.classIChatBaseComponent);
		
		this.constructorPacketPlayOutTitleTimes = ReflectionUtil.getConstructor(this.classPacketPlayOutTitle, this.classEnumTitleAction, this.classIChatBaseComponent, int.class, int.class, int.class);
		
		// Get Chat packet and it's constructor
		this.classPacketPlayOutChat = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutChat");
		this.constructorPacketPlayOutChat = ReflectionUtil.getConstructor(this.classPacketPlayOutChat, this.classIChatBaseComponent);
	}
	
	// -------------------------------------------- //
	// TO COMPONENT
	// -------------------------------------------- //

	protected Object toComponent(String raw)
	{
		return ReflectionUtil.invokeMethod(this.methodChatSerializer, null, raw);
	}
	
	// -------------------------------------------- //
	// CHAT
	// -------------------------------------------- //
	
	@Override
	public void sendChatMson(Object sendeeObject, Mson mson)
	{
		CommandSender sendee = IdUtil.getSender(sendeeObject);
		if (sendee == null) return;
		
		if (sendee instanceof Player)
		{
			Player player = (Player)sendee;
			String raw = mson.toRaw();
			Object component = toComponent(raw);
			Object packet = ReflectionUtil.invokeConstructor(this.constructorPacketPlayOutChat, component);
			NmsBasics.get().sendPacket(player, packet);
		}
		else
		{
			String plain = mson.toPlain(true);
			this.sendChatPlain(sendee, plain);
		}
	}
	
	// -------------------------------------------- //
	// TITLE
	// -------------------------------------------- //
	
	@Override
	public void sendTitleRaw(Object sendeeObject, int ticksIn, int ticksStay, int ticksOut, String rawMain, String rawSub)
	{
		Player player = IdUtil.getPlayer(sendeeObject);
		if (player == null) return;
		
		Object component;
		Object packet;
		Enum<?> action;
		
		// in, stay, out
		packet = ReflectionUtil.invokeConstructor(this.constructorPacketPlayOutTitleTimes, this.enumEnumTitleActionTimes, null, ticksIn, ticksStay, ticksOut);
		NmsBasics.get().sendPacket(player, packet);

		// main
		if (rawMain != null)
		{
			component = toComponent(rawMain);
			action = this.enumEnumTitleActionMain;
			packet = ReflectionUtil.invokeConstructor(this.constructorPacketPlayOutTitle, action, component);
			NmsBasics.get().sendPacket(player, packet);
		}
		
		// sub
		if (rawSub != null)
		{
			component = toComponent(rawSub);
			action = this.enumEnumTitleActionSub;
			packet = ReflectionUtil.invokeConstructor(constructorPacketPlayOutTitle, action, component);
			NmsBasics.get().sendPacket(player, packet);
		}
	}
	
	// -------------------------------------------- //
	// ACTIONBAR
	// -------------------------------------------- //
	
	@Override
	public void sendActionbarRaw(Object sendeeObject, String raw)
	{
		Player player = IdUtil.getPlayer(sendeeObject);
		if (player == null) return;
		
		Object component = toComponent(raw);
		Object packet = this.constructActionBarPacket(component);
		NmsBasics.get().sendPacket(player, packet);
	}
	
	public <T> T constructActionBarPacket(Object component) {
		return ReflectionUtil.invokeConstructor(this.constructorPacketPlayOutChatType, component, (byte)2);
	}
	
}
