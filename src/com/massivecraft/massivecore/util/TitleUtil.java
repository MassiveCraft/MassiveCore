package com.massivecraft.massivecore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;

public final class TitleUtil
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private static boolean useTitles = false;
	
	// The enums used to tell which packet it is.
	// They correspond to the commands with the same name.
	private static Class<?> titleEnumClass;
	private static Enum<?> titleEnum;
	private static Enum<?> subtitleEnum;
	private static Enum<?> timesEnum;
	
	// Method used to prepare text so it can be send
	private static Method chatSerializer;
	// The object we send instead of a string
	private static Class<?> iChatBaseComponent;
	
	// Handling the players conenction
	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;
	
	// The packet and its constructor
	private static Constructor<?> packetConstructor;
	private static Constructor<?> packetConstructorTimes;
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	static 
	{
		try
		{
			// The enum used
			titleEnumClass = PackageType.MINECRAFT_SERVER.getClass("EnumTitleAction");
			// Get the enum values.
			for (Object o : titleEnumClass.getEnumConstants())
			{
				Enum<?> e = (Enum<?>) o;
				if (e.name().equalsIgnoreCase("TITLE")) titleEnum = e;
				else if (e.name().equalsIgnoreCase("SUBTITLE")) subtitleEnum = e;
				else if (e.name().equalsIgnoreCase("TIMES")) timesEnum = e;
			}
			
			// Get chatserializer and chat component.
			iChatBaseComponent = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent");
			chatSerializer = PackageType.MINECRAFT_SERVER.getClass("ChatSerializer").getDeclaredMethod("a", String.class);
			
			// Get packet and it's constructor
			Class<?> packetClass = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle");
			packetConstructor = ReflectionUtils.getConstructor(packetClass, titleEnumClass, iChatBaseComponent);
			packetConstructorTimes = ReflectionUtils.getConstructor(packetClass, titleEnumClass, iChatBaseComponent, Integer.class, Integer.class, Integer.class);
			
			// Player connection
			getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
			playerConnection = ReflectionUtils.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, false, "playerConnection");
			sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", PackageType.MINECRAFT_SERVER.getClass("Packet"));
			
			// Set accesible
			chatSerializer.setAccessible(true);
			packetConstructor.setAccessible(true);
			packetConstructorTimes.setAccessible(true);
			getHandle.setAccessible(true);
			playerConnection.setAccessible(true);
			sendPacket.setAccessible(true);
			
			
			// This suceeded, we use titles.
			useTitles = true;
		}
		catch (Exception e)
		{
			MassiveCore.get().log(Level.INFO, "If you use 1.7.X or below, disregard this error");
			MassiveCore.get().log(Level.INFO, "If you use 1.8.X or above, please report at https://github.com/MassiveCraft/MassiveCore/issues");
			e.printStackTrace();
			MassiveCore.get().log(Level.INFO, "If you use 1.7.X or below, disregard this error");
			MassiveCore.get().log(Level.INFO, "If you use 1.8.X or above, please report at https://github.com/MassiveCraft/MassiveCore/issues");
		
			// It didn't suceed, we will not use titles.
			useTitles = false;
		}

	}
	
	// -------------------------------------------- //
	// AVAILABLE
	// -------------------------------------------- //
	
	public static boolean isAvailable()
	{
		return useTitles;
	}
	
	// -------------------------------------------- //
	// SEND TITLES
	// -------------------------------------------- //
	
	public static boolean sendTitle(Player player, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		if ( ! useTitles)
		{
			return false;
		}
		
		try
		{
			// Fadein, stay, fadeout
			Object timesPacket = packetConstructorTimes.newInstance(timesEnum, null, ticksIn, ticksStay, ticksOut);
			sendPacket.invoke(playerConnection.get( getHandle.invoke(player) ), timesPacket);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// So we failed, didn't work.
			return false;
		}
		
		if (titleMain != null)
		{
			titleMain = toJson(titleMain);
			try
			{
				// Title
				Object titleMainChat = chatSerializer.invoke(null, titleMain);
				Object titleMainPacket = packetConstructor.newInstance(titleEnum, titleMainChat);
				sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), titleMainPacket);	
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// So we failed, didn't work.
				return false;
			}
		}

		if (titleSub != null)
		{
			titleSub = toJson(titleSub);
			try
			{
				// SubTitle
				Object titleSubChat = chatSerializer.invoke(null, titleSub);
				Object titleSubPacket = packetConstructor.newInstance(subtitleEnum, titleSubChat);
				sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), titleSubPacket);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// So we failed, didn't work.
				return false;
			}
		}
		
		//It worked.
		return true;	
    }
	
	// -------------------------------------------- //
	// JSON
	// -------------------------------------------- //
	
	public static String toJson(String str)
	{
		str = JSONObject.escape(str);
		
		str = "{\"text\": \"" + str + "\"}";
		
		return str;
	}
	
}
