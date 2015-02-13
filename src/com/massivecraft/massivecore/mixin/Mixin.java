package com.massivecraft.massivecore.mixin;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.Predictate;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.teleport.PSGetter;

public class Mixin
{
	// -------------------------------------------- //
	// GET/SET MIXINS
	// -------------------------------------------- //
	
	private static WorldMixin worldMixin = WorldMixinDefault.get();
	public static WorldMixin getWorldMixin() { return worldMixin; }
	public static void setWorldMixin(WorldMixin val) { worldMixin = val; }
	
	private static DisplayNameMixin displayNameMixin = DisplayNameMixinDefault.get();
	public static DisplayNameMixin getDisplayNameMixin() { return displayNameMixin; }
	public static void setDisplayNameMixin(DisplayNameMixin val) { displayNameMixin = val; }
	
	private static InventoryMixin inventoryMixin = InventoryMixinDefault.get();
	public static InventoryMixin getInventoryMixin() { return inventoryMixin; }
	public static void setInventoryMixin(InventoryMixin val) { inventoryMixin = val; }
	
	private static SenderPsMixin senderPsMixin = SenderPsMixinDefault.get();
	public static SenderPsMixin getSenderPsMixin() { return senderPsMixin; }
	public static void setSenderPsMixin(SenderPsMixin val) { senderPsMixin = val; }
	
	private static PlayedMixin playedMixin = PlayedMixinDefault.get();
	public static PlayedMixin getPlayedMixin() { return playedMixin; }
	public static void setPlayedMixin(PlayedMixin val) { playedMixin = val; }
	
	private static VisibilityMixin visibilityMixin = VisibilityMixinDefault.get();
	public static VisibilityMixin getVisibilityMixin() { return visibilityMixin; }
	public static void setVisibilityMixin(VisibilityMixin val) { visibilityMixin = val; }
	
	private static TeleportMixin teleportMixin = TeleportMixinDefault.get();
	public static TeleportMixin getTeleportMixin() { return teleportMixin; }
	public static void setTeleportMixin(TeleportMixin val) { teleportMixin = val; }
	
	private static MessageMixin messageMixin = MessageMixinDefault.get();
	public static MessageMixin getMessageMixin() { return messageMixin; }
	public static void setMessageMixin(MessageMixin val) { messageMixin = val; }
	
	private static TitleMixin titleMixin = TitleMixinDefault.get();
	public static TitleMixin getTitleMixin() { return titleMixin; }
	public static void setTitleMixin(TitleMixin val) { titleMixin = val; }
	
	private static KickMixin kickMixin = KickMixinDefault.get();
	public static KickMixin getKickMixin() { return kickMixin; }
	public static void setKickMixin(KickMixin val) { kickMixin = val; }
	
	private static ActualMixin actualMixin = ActualMixinDefault.get();
	public static ActualMixin getActualMixin() { return actualMixin; }
	public static void setActualMixin(ActualMixin val) { actualMixin = val; }
	
	private static CommandMixin commandMixin = CommandMixinDefault.get();
	public static CommandMixin getCommandMixin() { return commandMixin; }
	public static void setCommandMixin(CommandMixin val) { commandMixin = val; }
	
	// -------------------------------------------- //
	// STATIC EXPOSE: WORLD
	// -------------------------------------------- //
	
	public static boolean canSeeWorld(Permissible permissible, String worldId)
	{
		return getWorldMixin().canSeeWorld(permissible, worldId);
	}
	
	public static List<String> getWorldIds()
	{
		return getWorldMixin().getWorldIds();
	}
	
	public static List<String> getVisibleWorldIds(Permissible permissible)
	{
		return getWorldMixin().getVisibleWorldIds(permissible);
	}
	
	public static ChatColor getWorldColor(String worldId)
	{
		return getWorldMixin().getWorldColor(worldId);
	}
	
	public static List<String> getWorldAliases(String worldId)
	{
		return getWorldMixin().getWorldAliases(worldId);
	}
	
	public static String getWorldAliasOrId(String worldId)
	{
		return getWorldMixin().getWorldAliasOrId(worldId);
	}
	
	public static String getWorldDisplayName(String worldId)
	{
		return getWorldMixin().getWorldDisplayName(worldId);
	}
	
	public static PS getWorldSpawnPs(String worldId)
	{
		return getWorldMixin().getWorldSpawnPs(worldId);
	}
	
	public static void setWorldSpawnPs(String worldId, PS spawnPs)
	{
		getWorldMixin().setWorldSpawnPs(worldId, spawnPs);
	}
	
	public static boolean trySetWorldSpawnWp(CommandSender sender, String worldId, PS spawnPs, boolean verbooseChange, boolean verbooseSame)
	{
		return getWorldMixin().trySetWorldSpawnWp(sender, worldId, spawnPs, verbooseChange, verbooseSame);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: DISPLAY NAME
	// -------------------------------------------- //
	
	@Deprecated
	public static String getDisplayName(Object senderObject)
	{
		return getDisplayNameMixin().getDisplayName(senderObject, null);
	}
	
	public static String getDisplayName(Object senderObject, Object watcherObject)
	{
		return getDisplayNameMixin().getDisplayName(senderObject, watcherObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: INVENTORY
	// -------------------------------------------- //
	
	public static PlayerInventory createPlayerInventory()
	{
		return getInventoryMixin().createPlayerInventory();
	}
	
	public static Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return getInventoryMixin().createInventory(holder, size, title);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: SENDER PS
	// -------------------------------------------- //
	
	public static PS getSenderPs(Object senderObject)
	{
		return getSenderPsMixin().getSenderPs(senderObject);
	}

	public static void setSenderPs(Object senderObject, PS ps)
	{
		getSenderPsMixin().setSenderPs(senderObject, ps);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: PLAYED
	// -------------------------------------------- //
	
	public static boolean isOnline(Object senderObject)
	{
		return getPlayedMixin().isOnline(senderObject);
	}
	public static boolean isOffline(Object senderObject)
	{
		return getPlayedMixin().isOffline(senderObject);
	}
	public static Long getLastPlayed(Object senderObject)
	{
		return getPlayedMixin().getLastPlayed(senderObject);
	}
	public static Long getFirstPlayed(Object senderObject)
	{
		return getPlayedMixin().getFirstPlayed(senderObject);
	}
	public static boolean hasPlayedBefore(Object senderObject)
	{
		return getPlayedMixin().hasPlayedBefore(senderObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: VISIBILITY
	// -------------------------------------------- //
	
	public static boolean canSee(Object watcherObject, Object watcheeObject)
	{
		return getVisibilityMixin().canSee(watcherObject, watcheeObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: TELEPORTER
	// -------------------------------------------- //
	
	public static boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return getTeleportMixin().isCausedByMixin(event);
	}
	
	// PS
	public static void teleport(Object teleporteeObject, PS to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to);
	}
	public static void teleport(Object teleporteeObject, PS to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc);
	}
	public static void teleport(Object teleporteeObject, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delaySeconds);
	}
	
	// CommandSender
	public static void teleport(Object teleporteeObject, CommandSender to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to);
	}
	public static void teleport(Object teleporteeObject, CommandSender to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc);
	}
	public static void teleport(Object teleporteeObject, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delaySeconds);
	}
	
	// SenderEntity
	public static void teleport(Object teleporteeObject, SenderEntity<?> to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to);
	}
	public static void teleport(Object teleporteeObject, SenderEntity<?> to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc);
	}
	public static void teleport(Object teleporteeObject, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delaySeconds);
	}
	
	// String
	public static void teleport(Object teleporteeObject, String to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to);
	}
	public static void teleport(Object teleporteeObject, String to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc);
	}
	public static void teleport(Object teleporteeObject, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, String to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delaySeconds);
	}
	
	// PSGetter
	public static void teleport(Object teleporteeObject, PSGetter to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to);
	}
	public static void teleport(Object teleporteeObject, PSGetter to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc);
	}
	public static void teleport(Object teleporteeObject, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleporteeObject, to, desc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: MESSAGE
	// -------------------------------------------- //
	
	// All
	public static boolean messageAll(String message)
	{
		return getMessageMixin().messageAll(message);
	}
	public static boolean messageAll(String... messages)
	{
		return getMessageMixin().messageAll(messages);
	}
	public static boolean messageAll(Collection<String> messages)
	{
		return getMessageMixin().messageAll(messages);
	}
	
	// Predictate
	public static boolean messagePredictate(Predictate<CommandSender> predictate, String message)
	{
		return getMessageMixin().messagePredictate(predictate, message);
	}
	public static boolean messagePredictate(Predictate<CommandSender> predictate, String... messages)
	{
		return getMessageMixin().messagePredictate(predictate, messages);
	}
	public static boolean messagePredictate(Predictate<CommandSender> predictate, Collection<String> messages)
	{
		return getMessageMixin().messagePredictate(predictate, messages);
	}
	
	// One
	public static boolean messageOne(Object senderObject, String message)
	{
		return getMessageMixin().messageOne(senderObject, message);
	}
	public static boolean messageOne(Object senderObject, String... messages)
	{
		return getMessageMixin().messageOne(senderObject, messages);
	}
	public static boolean messageOne(Object senderObject, Collection<String> messages)
	{
		return getMessageMixin().messageOne(senderObject, messages);
	}
	
	// All
	public static boolean msgAll(String msg)
	{
		return getMessageMixin().msgAll(msg);
	}
	public static boolean msgAll(String msg, Object... args)
	{
		return getMessageMixin().msgAll(msg, args);
	}
	public static boolean msgAll(Collection<String> msgs)
	{
		return getMessageMixin().msgAll(msgs);
	}
	
	// Predictate
	public static boolean msgPredictate(Predictate<CommandSender> predictate, String msg)
	{
		return getMessageMixin().msgPredictate(predictate, msg);
	}
	public static boolean msgPredictate(Predictate<CommandSender> predictate, String msg, Object... args)
	{
		return getMessageMixin().msgPredictate(predictate, msg, args);
	}
	public static boolean msgPredictate(Predictate<CommandSender> predictate, Collection<String> msgs)
	{
		return getMessageMixin().msgPredictate(predictate, msgs);
	}
	
	// One
	public static boolean msgOne(Object senderObject, String msg)
	{
		return getMessageMixin().msgOne(senderObject, msg);
	}
	public static boolean msgOne(Object senderObject, String msg, Object... args)
	{
		return getMessageMixin().msgOne(senderObject, msg, args);
	}
	public static boolean msgOne(Object senderObject, Collection<String> msgs)
	{
		return getMessageMixin().msgOne(senderObject, msgs);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: TITLE
	// -------------------------------------------- //
	
	// Default
	public static boolean sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return getTitleMixin().sendTitleMessage(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	// Parsed
	public static boolean sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return getTitleMixin().sendTitleMsg(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	// Available
	public static boolean isTitlesAvailable()
	{
		return getTitleMixin().isTitlesAvailable();
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: KICK
	// -------------------------------------------- //
	
	public static boolean kick(Object senderObject)
	{
		return getKickMixin().kick(senderObject);
	}
	
	public static boolean kick(Object senderObject, String message)
	{
		return getKickMixin().kick(senderObject, message);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: ACTUALL
	// -------------------------------------------- //
	
	public static boolean isActualJoin(PlayerJoinEvent event)
	{
		return getActualMixin().isActualJoin(event);
	}
	
	public static boolean isActualLeave(EventMassiveCorePlayerLeave event)
	{
		return getActualMixin().isActualLeave(event);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: COMMAND
	// -------------------------------------------- //
	
	public static boolean dispatchCommand(Object senderObject, String commandLine)
	{
		return getCommandMixin().dispatchCommand(senderObject, commandLine);
	}
	
	public static boolean dispatchCommand(Object presentObject, Object senderObject, String commandLine)
	{
		return getCommandMixin().dispatchCommand(presentObject, senderObject, commandLine);
	}
	
}
