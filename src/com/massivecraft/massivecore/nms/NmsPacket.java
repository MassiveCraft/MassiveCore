package com.massivecraft.massivecore.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.massivecraft.massivecore.util.Txt;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils;
import com.massivecraft.massivecore.particleeffect.ReflectionUtils.PackageType;

public final class NmsPacket extends NmsAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static NmsPacket i = new NmsPacket();
	public static NmsPacket get () { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// The enums used to tell which packet it is.
	// They correspond to the commands with the same name.
	private static Class<?> titleEnumClass;
	private static Enum<?> titleMainEnum;
	private static Enum<?> titleSubEnum;
	private static Enum<?> titleTimesEnum;

	// Method used to prepare text so it can be sent
	private static Method chatSerializer;
	// The object we send instead of a string
	private static Class<?> iChatBaseComponent;

	// Handling the players conenction
	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;

	// The title packet and its constructor
	private static Constructor<?> titlePacketConstructor;
	private static Constructor<?> titlePacketConstructorTimes;

	// The chat packet and its constructors
	private static Constructor<?> chatPacketConstructor;
	private static Constructor<?> chatPacketActionbarConstructor;

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getRequiredVersion()
	{
		return 8;
	}
	
	@Override
	protected void setup() throws Throwable
	{
		// The enum used for titles
		titleEnumClass = getTitleEnumClass();

		// Get the title enum values.
		for (Object o : titleEnumClass.getEnumConstants())
		{
			Enum<?> e = (Enum<?>) o;
			if (e.name().equalsIgnoreCase("TITLE")) titleMainEnum = e;
			else if (e.name().equalsIgnoreCase("SUBTITLE")) titleSubEnum = e;
			else if (e.name().equalsIgnoreCase("TIMES")) titleTimesEnum = e;
		}

		// Get chatserializer and chat component.
		iChatBaseComponent = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent");

		chatSerializer = getChatSerializer();

		// Get title packet and it's constructor
		Class<?> titlePacketClass = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle");
		titlePacketConstructor = ReflectionUtils.getConstructor(titlePacketClass, titleEnumClass, iChatBaseComponent);
		titlePacketConstructorTimes = ReflectionUtils.getConstructor(titlePacketClass, titleEnumClass, iChatBaseComponent, Integer.class, Integer.class, Integer.class);

		// Get Chat packet and it's constructor
		Class<?> chatPacketClass = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutChat");
		chatPacketConstructor = ReflectionUtils.getConstructor(chatPacketClass, iChatBaseComponent);
		chatPacketActionbarConstructor = ReflectionUtils.getConstructor(chatPacketClass, iChatBaseComponent, Byte.TYPE);

		// Player connection
		getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
		playerConnection = ReflectionUtils.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, false, "playerConnection");
		sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", PackageType.MINECRAFT_SERVER.getClass("Packet"));

		// Set accessible
		setAllAccessible();
	}

	public static Class<?> getTitleEnumClass() throws ClassNotFoundException
	{
		Class<?> ret;
		try
		{
			ret = titleEnumClass = PackageType.MINECRAFT_SERVER.getClass("EnumTitleAction");
		}
		catch (ClassNotFoundException e)
		{
			// Since 1.8.3
			ret = titleEnumClass = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutTitle$EnumTitleAction");
		}

		return ret;
	}

	public static Method getChatSerializer() throws Exception
	{
		Method ret;
		try
		{
			ret = chatSerializer = PackageType.MINECRAFT_SERVER.getClass("ChatSerializer").getDeclaredMethod("a", String.class);
		}
		catch (ClassNotFoundException e)
		{
			// Since 1.8.3
			ret = chatSerializer = PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer").getDeclaredMethod("a", String.class);
		}

		return ret;
	}

	public static void setAllAccessible()
	{
		chatSerializer.setAccessible(true);
		titlePacketConstructor.setAccessible(true);
		titlePacketConstructorTimes.setAccessible(true);
		chatPacketConstructor.setAccessible(true);
		getHandle.setAccessible(true);
		playerConnection.setAccessible(true);
		sendPacket.setAccessible(true);
	}

	// -------------------------------------------- //
	// SEND TITLES
	// -------------------------------------------- //

	public static boolean sendTitle(Player player, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		if ( ! get().isAvailable()) return false;

		try
		{
			// Fadein, stay, fadeout
			Object timesPacket = titlePacketConstructorTimes.newInstance(titleTimesEnum, null, ticksIn, ticksStay, ticksOut);
			sendPacket(player, timesPacket);

			if (titleMain != null)
			{
				// Title
				Object titleMainChat = toChatBaseComponent(titleMain);
				Object titleMainPacket = titlePacketConstructor.newInstance(titleMainEnum, titleMainChat);
				sendPacket(player, titleMainPacket);

			}

			if (titleSub != null)
			{
				// SubTitle
				Object titleSubChat = toChatBaseComponent(titleSub);
				Object titleSubPacket = titlePacketConstructor.newInstance(titleSubEnum, titleSubChat);
				sendPacket(player, titleSubPacket);
			}

		}
		catch (Exception ex)
		{
			MassiveCore.get().log(Txt.parse("<b>Sending title failed!"));
			ex.printStackTrace();
			// So we failed, didn't work.
			return false;
		}

		//It worked.
		return true;
	}

	// -------------------------------------------- //
	// SEND RAW
	// -------------------------------------------- //

	public static boolean sendRaw(Player player, String string)
	{
		if ( ! get().isAvailable()) return false;

		try
		{
			Object rawChat = toChatBaseComponent(string);
			Object chatPacket = chatPacketConstructor.newInstance(rawChat);

			sendPacket(player, chatPacket);
		}
		catch (Exception ex)
		{
			MassiveCore.get().log(Txt.parse("<b>Sending raw chat failed!"));
			ex.printStackTrace();
			// So we failed and it didn't work.
			return false;
		}

		return true;
	}

	// -------------------------------------------- //
	// SEND ACTIONBAR
	// -------------------------------------------- //

	public static boolean sendActionbar(Player player, String string)
	{
		if ( ! get().isAvailable()) return false;

		try
		{
			Object actionbar = toChatBaseComponent(string);
			Object chatPacket = chatPacketActionbarConstructor.newInstance(actionbar, (byte) 2);

			sendPacket(player, chatPacket);
		}
		catch(Exception ex)
		{
			MassiveCore.get().log(Txt.parse("<b>Sending actionbar failed!"));
			ex.printStackTrace();
			// So we failed and it didn't work.
			return false;
		}

		return true;
	}

	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //

	public static void sendPacket(Player player, Object packet) throws Exception
	{
		sendPacket.invoke(getPlayerConnection(player), packet);
	}

	public static Object getPlayerConnection(Player player) throws Exception
	{
		return playerConnection.get(getHandle.invoke(player));
	}

	public static Object toChatBaseComponent(String str) throws Exception
	{
		return chatSerializer.invoke(null, str);
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
