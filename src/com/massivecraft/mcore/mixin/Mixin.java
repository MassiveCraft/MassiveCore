package com.massivecraft.mcore.mixin;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permissible;

import com.massivecraft.mcore.Predictate;
import com.massivecraft.mcore.event.MCorePlayerLeaveEvent;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.teleport.PSGetter;

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
	
	private static ListNameMixin listNameMixin = ListNameMixinDefault.get();
	public static ListNameMixin getListNameMixin() { return listNameMixin; }
	public static void setListNameMixin(ListNameMixin val) { listNameMixin = val; }
	
	private static SenderPsMixin senderPsMixin = SenderPsMixinDefault.get();
	public static SenderPsMixin getSenderPsMixin() { return senderPsMixin; }
	public static void setSenderPsMixin(SenderPsMixin val) { senderPsMixin = val; }
	
	private static PlayedMixin playedMixin = PlayedMixinDefault.get();
	public static PlayedMixin getPlayedMixin() { return playedMixin; }
	public static void setPlayedMixin(PlayedMixin val) { playedMixin = val; }
	
	private static VisibilityMixin visibilityMixin = VisibilityMixinDefault.get();
	public static VisibilityMixin getVisibilityMixin() { return visibilityMixin; }
	public static void setVisibilityMixin(VisibilityMixin val) { visibilityMixin = val; }
	
	private static SenderIdMixin senderIdMixin = SenderIdMixinDefault.get();
	public static SenderIdMixin getSenderIdMixin() { return senderIdMixin; }
	public static void setSenderIdMixin(SenderIdMixin val) { senderIdMixin = val; }
	
	private static TeleportMixin teleportMixin = TeleportMixinDefault.get();
	public static TeleportMixin getTeleportMixin() { return teleportMixin; }
	public static void setTeleportMixin(TeleportMixin val) { teleportMixin = val; }
	
	private static MessageMixin messageMixin = MessageMixinDefault.get();
	public static MessageMixin getMessageMixin() { return messageMixin; }
	public static void setMessageMixin(MessageMixin val) { messageMixin = val; }
	
	private static KickMixin kickMixin = KickMixinDefault.get();
	public static KickMixin getKickMixin() { return kickMixin; }
	public static void setKickMixin(KickMixin val) { kickMixin = val; }
	
	private static ActualMixin actualMixin = ActualMixinDefault.get();
	public static ActualMixin getActualMixin() { return actualMixin; }
	public static void setActualMixin(ActualMixin val) { actualMixin = val; }
	
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
	
	public static String getDisplayName(String senderId)
	{
		return getDisplayNameMixin().getDisplayName(senderId);
	}
	
	public static void setDisplayName(String senderId, String displayName)
	{
		getDisplayNameMixin().setDisplayName(senderId, displayName);
	}
	
	public static String getDisplayName(CommandSender sender)
	{
		return getDisplayNameMixin().getDisplayName(sender);
	}
	
	public static void setDisplayName(CommandSender sender, String displayName)
	{
		getDisplayNameMixin().setDisplayName(sender, displayName);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: LIST NAME
	// -------------------------------------------- //
	
	public static String getListName(String senderId)
	{
		return getListNameMixin().getListName(senderId);
	}
	
	public static void setListName(String senderId, String listName)
	{
		getListNameMixin().setListName(senderId, listName);
	}
	
	public static String getListName(CommandSender sender)
	{
		return getListNameMixin().getListName(sender);
	}
	
	public static void setListName(CommandSender sender, String listName)
	{
		getListNameMixin().setListName(sender, listName);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: SENDER PS
	// -------------------------------------------- //
	
	public static PS getSenderPs(String senderId)
	{
		return getSenderPsMixin().getSenderPs(senderId);
	}

	public static void setSenderPs(String senderId, PS ps)
	{
		getSenderPsMixin().setSenderPs(senderId, ps);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: PLAYED
	// -------------------------------------------- //
	
	public static boolean isOnline(String senderId)
	{
		return getPlayedMixin().isOnline(senderId);
	}
	public static boolean isOffline(String senderId)
	{
		return getPlayedMixin().isOffline(senderId);
	}
	public static Long getLastPlayed(String senderId)
	{
		return getPlayedMixin().getLastPlayed(senderId);
	}
	public static Long getFirstPlayed(String senderId)
	{
		return getPlayedMixin().getFirstPlayed(senderId);
	}
	public static boolean hasPlayedBefore(String senderId)
	{
		return getPlayedMixin().hasPlayedBefore(senderId);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: VISIBILITY
	// -------------------------------------------- //
	
	public static boolean canSee(String watcherId, String watcheeId)
	{
		return getVisibilityMixin().canSee(watcherId, watcheeId);
	}
	public static boolean canSee(CommandSender watcher, String watcheeId)
	{
		return getVisibilityMixin().canSee(watcher, watcheeId);
	}
	public static boolean canSee(String watcherId, CommandSender watchee)
	{
		return getVisibilityMixin().canSee(watcherId, watchee);
	}
	public static boolean canSee(CommandSender watcher, CommandSender watchee)
	{
		return getVisibilityMixin().canSee(watcher, watchee);
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
	
	public static Set<String> getAllPlayerIds()
	{
		return getSenderIdMixin().getAllPlayerIds();
	}
	public static Set<String> getOnlinePlayerIds()
	{
		return getSenderIdMixin().getOnlinePlayerIds();
	}
	public static Set<String> getOfflinePlayerIds()
	{
		return getSenderIdMixin().getOfflinePlayerIds();
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: TELEPORTER
	// -------------------------------------------- //
	
	public static boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return getTeleportMixin().isCausedByMixin(event);
	}
	
	// CommandSender & PS
	public static void teleport(CommandSender teleportee, PS to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(CommandSender teleportee, PS to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(CommandSender teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(CommandSender teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// CommandSender & CommandSender
	public static void teleport(CommandSender teleportee, CommandSender to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(CommandSender teleportee, CommandSender to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(CommandSender teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(CommandSender teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// CommandSender & SenderEntity
	public static void teleport(CommandSender teleportee, SenderEntity<?> to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(CommandSender teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(CommandSender teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// CommandSender & String
	public static void teleport(CommandSender teleportee, String to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(CommandSender teleportee, String to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(CommandSender teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(CommandSender teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// CommandSender & PSGetter
	public static void teleport(CommandSender teleportee, PSGetter to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(CommandSender teleportee, PSGetter to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(CommandSender teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(CommandSender teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// SenderEntity & PS
	public static void teleport(SenderEntity<?> teleportee, PS to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(SenderEntity<?> teleportee, PS to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(SenderEntity<?> teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(SenderEntity<?> teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// SenderEntity & CommandSender
	public static void teleport(SenderEntity<?> teleportee, CommandSender to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(SenderEntity<?> teleportee, CommandSender to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(SenderEntity<?> teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// SenderEntity & SenderEntity
	public static void teleport(SenderEntity<?> teleportee, SenderEntity<?> to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(SenderEntity<?> teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// SenderEntity & String
	public static void teleport(SenderEntity<?> teleportee, String to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(SenderEntity<?> teleportee, String to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(SenderEntity<?> teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(SenderEntity<?> teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// SenderEntity & PSGetter
	public static void teleport(SenderEntity<?> teleportee, PSGetter to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(SenderEntity<?> teleportee, PSGetter to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(SenderEntity<?> teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// String & PS
	public static void teleport(String teleportee, PS to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(String teleportee, PS to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(String teleportee, PS to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(String teleportee, PS to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// String & CommandSender
	public static void teleport(String teleportee, CommandSender to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(String teleportee, CommandSender to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(String teleportee, CommandSender to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(String teleportee, CommandSender to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// String & SenderEntity
	public static void teleport(String teleportee, SenderEntity<?> to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(String teleportee, SenderEntity<?> to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(String teleportee, SenderEntity<?> to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(String teleportee, SenderEntity<?> to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// String & String
	public static void teleport(String teleportee, String to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(String teleportee, String to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(String teleportee, String to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(String teleportee, String to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// String & PSGetter
	public static void teleport(String teleportee, PSGetter to) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to);
	}
	public static void teleport(String teleportee, PSGetter to, String desc) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc);
	}
	public static void teleport(String teleportee, PSGetter to, String desc, Permissible delayPermissible) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delayPermissible);
	}
	public static void teleport(String teleportee, PSGetter to, String desc, int delaySeconds) throws TeleporterException
	{
		getTeleportMixin().teleport(teleportee, to, desc, delaySeconds);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: MESSAGE
	// -------------------------------------------- //
	
	// All
	public static boolean message(String message)
	{
		return getMessageMixin().message(message);
	}
	public static boolean message(String... messages)
	{
		return getMessageMixin().message(messages);
	}
	public static boolean message(Collection<String> messages)
	{
		return getMessageMixin().message(messages);
	}
	
	// Predictate
	public static boolean message(Predictate<CommandSender> predictate, String message)
	{
		return getMessageMixin().message(predictate, message);
	}
	public static boolean message(Predictate<CommandSender> predictate, String... messages)
	{
		return getMessageMixin().message(predictate, messages);
	}
	public static boolean message(Predictate<CommandSender> predictate, Collection<String> messages)
	{
		return getMessageMixin().message(predictate, messages);
	}
	
	// One
	public static boolean message(CommandSender sender, String message)
	{
		return getMessageMixin().message(sender, message);
	}
	public static boolean message(CommandSender sender, String... messages)
	{
		return getMessageMixin().message(sender, messages);
	}
	public static boolean message(CommandSender sender, Collection<String> messages)
	{
		return getMessageMixin().message(sender, messages);
	}
	
	// One by id
	public static boolean message(String senderId, String message)
	{
		return getMessageMixin().message(senderId, message);
	}
	public static boolean message(String senderId, String... messages)
	{
		return getMessageMixin().message(senderId, messages);
	}
	public static boolean message(String senderId, Collection<String> messages)
	{
		return getMessageMixin().message(senderId, messages);
	}
	
	// All
	public static boolean msg(String msg)
	{
		return getMessageMixin().msg(msg);
	}
	public static boolean msg(String msg, Object... args)
	{
		return getMessageMixin().msg(msg, args);
	}
	public static boolean msg(Collection<String> msgs)
	{
		return getMessageMixin().msg(msgs);
	}
	
	// Predictate
	public static boolean msg(Predictate<CommandSender> predictate, String msg)
	{
		return getMessageMixin().msg(predictate, msg);
	}
	public static boolean msg(Predictate<CommandSender> predictate, String msg, Object... args)
	{
		return getMessageMixin().msg(predictate, msg, args);
	}
	public static boolean msg(Predictate<CommandSender> predictate, Collection<String> msgs)
	{
		return getMessageMixin().msg(predictate, msgs);
	}
	
	// One
	public static boolean msg(CommandSender sender, String msg)
	{
		return getMessageMixin().msg(sender, msg);
	}
	public static boolean msg(CommandSender sender, String msg, Object... args)
	{
		return getMessageMixin().msg(sender, msg, args);
	}
	public static boolean msg(CommandSender sender, Collection<String> msgs)
	{
		return getMessageMixin().msg(sender, msgs);
	}
	
	// One by id
	public static boolean msg(String senderId, String msg)
	{
		return getMessageMixin().msg(senderId, msg);
	}
	public static boolean msg(String senderId, String msg, Object... args)
	{
		return getMessageMixin().msg(senderId, msg, args);
	}
	public static boolean msg(String senderId, Collection<String> msgs)
	{
		return getMessageMixin().msg(senderId, msgs);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: KICK
	// -------------------------------------------- //
	
	public static boolean kick(CommandSender sender)
	{
		return getKickMixin().kick(sender);
	}
	public static boolean kick(String senderId)
	{
		return getKickMixin().kick(senderId);
	}
	
	public static boolean kick(CommandSender sender, String message)
	{
		return getKickMixin().kick(sender, message);
	}
	public static boolean kick(String senderId, String message)
	{
		return getKickMixin().kick(senderId, message);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: ACTUALL
	// -------------------------------------------- //
	
	public static boolean isActualJoin(PlayerJoinEvent event)
	{
		return getActualMixin().isActualJoin(event);
	}
	
	public static boolean isActualLeave(MCorePlayerLeaveEvent event)
	{
		return getActualMixin().isActualLeave(event);
	}
	
}
