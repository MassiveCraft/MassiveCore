package com.massivecraft.mcore5.mixin;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.massivecraft.mcore5.PS;

public class Mixin
{
	// -------------------------------------------- //
	// GET/SET MIXINS
	// -------------------------------------------- //
	
	private static PsTeleporterMixin psTeleporterMixin = DefaultPsTeleporterMixin.get();
	public static PsTeleporterMixin getPsTeleporterMixin() { return psTeleporterMixin; }
	public static void setPsTeleporterMixin(PsTeleporterMixin val) { psTeleporterMixin = val; }
	
	private static DisplayNameMixin displayNameMixin = DefaultDisplayNameMixin.get();
	public static DisplayNameMixin getDisplayNameMixin() { return displayNameMixin; }
	public static void setDisplayNameMixin(DisplayNameMixin val) { displayNameMixin = val; }
	
	private static ListNameMixin listNameMixin = DefaultListNameMixin.get();
	public static ListNameMixin getListNameMixin() { return listNameMixin; }
	public static void setListNameMixin(ListNameMixin val) { listNameMixin = val; }
	
	private static SenderIdMixin senderIdMixin = DefaultSenderIdMixin.get();
	public static SenderIdMixin getSenderIdMixin() { return senderIdMixin; }
	public static void setSenderIdMixin(SenderIdMixin val) { senderIdMixin = val; }
	
	// -------------------------------------------- //
	// STATIC EXPOSE: PS TELEPORTER
	// -------------------------------------------- //
	
	public static void teleport(Entity entity, PS ps) throws PsTeleporterException
	{
		getPsTeleporterMixin().teleport(entity, ps);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: DISPLAY NAME
	// -------------------------------------------- //
	
	public static String getDisplayName(String senderId)
	{
		return getDisplayNameMixin().get(senderId);
	}
	
	public static void setDisplayName(String senderId, String displayName)
	{
		getDisplayNameMixin().set(senderId, displayName);
	}
	
	public static String getDisplayName(CommandSender sender)
	{
		return getDisplayNameMixin().get(sender);
	}
	
	public static void setDisplayName(CommandSender sender, String displayName)
	{
		getDisplayNameMixin().set(sender, displayName);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: LIST NAME
	// -------------------------------------------- //
	
	public static String getListName(String senderId)
	{
		return getListNameMixin().get(senderId);
	}
	
	public static void setListName(String senderId, String listName)
	{
		getListNameMixin().set(senderId, listName);
	}
	
	public static String getListName(CommandSender sender)
	{
		return getListNameMixin().get(sender);
	}
	
	public static void setListName(CommandSender sender, String listName)
	{
		getListNameMixin().set(sender, listName);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: SENDER ID
	// -------------------------------------------- //
	
	public static String reqFix(String senderId)
	{
		return getSenderIdMixin().reqFix(senderId);
	}
	public static String tryFix(String senderId)
	{
		return getSenderIdMixin().tryFix(senderId);
	}
	public static boolean canFix(String senderId)
	{
		return getSenderIdMixin().canFix(senderId);
	}
	
	public static boolean isOnline(String senderId)
	{
		return getSenderIdMixin().isOnline(senderId);
	}
	public static boolean isOffline(String senderId)
	{
		return getSenderIdMixin().isOffline(senderId);
	}
	public static boolean hasBeenOnline(String senderId)
	{
		return getSenderIdMixin().hasBeenOnline(senderId);
	}
	public static Long getLastOnline(String senderId)
	{
		return getSenderIdMixin().getLastOnline(senderId);
	}	
	
	public static Set<String> getAllSenderIds()
	{
		return getSenderIdMixin().getAllSenderIds();
	}
	public static Set<String> getOnlineSenderIds()
	{
		return getSenderIdMixin().getOnlineSenderIds();
	}
	public static Set<String> getOfflineSenderIds()
	{
		return getSenderIdMixin().getOfflineSenderIds();
	}
	
}
