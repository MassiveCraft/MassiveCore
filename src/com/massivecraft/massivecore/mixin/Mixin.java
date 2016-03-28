package com.massivecraft.massivecore.mixin;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

import com.massivecraft.massivecore.event.EventMassiveCorePlayerLeave;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.teleport.Destination;

public class Mixin
{
	// -------------------------------------------- //
	// STATIC EXPOSE: WORLD
	// -------------------------------------------- //
	
	public static boolean canSeeWorld(Permissible permissible, String worldId)
	{
		return MixinWorld.get().canSeeWorld(permissible, worldId);
	}
	
	public static List<String> getWorldIds()
	{
		return MixinWorld.get().getWorldIds();
	}
	
	public static List<String> getVisibleWorldIds(Permissible permissible)
	{
		return MixinWorld.get().getVisibleWorldIds(permissible);
	}
	
	public static ChatColor getWorldColor(String worldId)
	{
		return MixinWorld.get().getWorldColor(worldId);
	}
	
	public static List<String> getWorldAliases(String worldId)
	{
		return MixinWorld.get().getWorldAliases(worldId);
	}
	
	public static String getWorldAliasOrId(String worldId)
	{
		return MixinWorld.get().getWorldAliasOrId(worldId);
	}
	
	public static String getWorldDisplayName(String worldId)
	{
		return MixinWorld.get().getWorldDisplayName(worldId);
	}
	
	public static PS getWorldSpawnPs(String worldId)
	{
		return MixinWorld.get().getWorldSpawnPs(worldId);
	}
	
	public static void setWorldSpawnPs(String worldId, PS spawnPs)
	{
		MixinWorld.get().setWorldSpawnPs(worldId, spawnPs);
	}
	
	public static boolean trySetWorldSpawnWp(CommandSender sender, String worldId, PS spawnPs, boolean verbooseChange, boolean verbooseSame)
	{
		return MixinWorld.get().trySetWorldSpawnWp(sender, worldId, spawnPs, verbooseChange, verbooseSame);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: DISPLAY NAME
	// -------------------------------------------- //
	
	@Deprecated
	public static String getDisplayName(Object senderObject)
	{
		return MixinDisplayName.get().getDisplayName(senderObject, null);
	}
	
	public static String getDisplayName(Object senderObject, Object watcherObject)
	{
		return MixinDisplayName.get().getDisplayName(senderObject, watcherObject);
	}
	
	public static Mson getDisplayNameMson(Object senderObject, Object watcherObject)
	{
		return MixinDisplayName.get().getDisplayNameMson(senderObject, watcherObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: INVENTORY
	// -------------------------------------------- //
	
	public static PlayerInventory createPlayerInventory()
	{
		return MixinInventory.get().createPlayerInventory();
	}
	
	public static Inventory createInventory(InventoryHolder holder, int size, String title)
	{
		return MixinInventory.get().createInventory(holder, size, title);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: SENDER PS
	// -------------------------------------------- //
	
	public static PS getSenderPs(Object senderObject)
	{
		return MixinSenderPs.get().getSenderPs(senderObject);
	}
	
	public static void setSenderPs(Object senderObject, PS ps)
	{
		MixinSenderPs.get().setSenderPs(senderObject, ps);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: GAMEMODE
	// -------------------------------------------- //
	
	public static GameMode getGamemode(Object playerObject)
	{
		return MixinGamemode.get().getGamemode(playerObject);
	}
	
	public static void setGamemode(Object playerObject, GameMode gameMode)
	{
		MixinGamemode.get().setGamemode(playerObject, gameMode);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: PLAYED
	// -------------------------------------------- //
	
	public static boolean isOnline(Object senderObject)
	{
		return MixinPlayed.get().isOnline(senderObject);
	}
	public static boolean isOffline(Object senderObject)
	{
		return MixinPlayed.get().isOffline(senderObject);
	}
	public static Long getLastPlayed(Object senderObject)
	{
		return MixinPlayed.get().getLastPlayed(senderObject);
	}
	public static Long getFirstPlayed(Object senderObject)
	{
		return MixinPlayed.get().getFirstPlayed(senderObject);
	}
	public static boolean hasPlayedBefore(Object senderObject)
	{
		return MixinPlayed.get().hasPlayedBefore(senderObject);
	}
	public static String getIp(Object senderObject)
	{
		return MixinPlayed.get().getIp(senderObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: VISIBILITY
	// -------------------------------------------- //
	
	public static boolean isVisible(Object watcheeObject)
	{
		return MixinVisibility.get().isVisible(watcheeObject);
	}
	public static boolean isVisible(Object watcheeObject, Object watcherObject)
	{
		return MixinVisibility.get().isVisible(watcheeObject, watcherObject);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: TELEPORTER
	// -------------------------------------------- //
	
	public static boolean isCausedByMixin(PlayerTeleportEvent event)
	{
		return MixinTeleport.get().isCausedByMixin(event);
	}
	
	public static void teleport(Object teleporteeObject, Destination destination) throws TeleporterException
	{
		MixinTeleport.get().teleport(teleporteeObject, destination);
	}
	public static void teleport(Object teleporteeObject, Destination destination, Permissible delayPermissible) throws TeleporterException
	{
		MixinTeleport.get().teleport(teleporteeObject, destination, delayPermissible);
	}
	public static void teleport(Object teleporteeObject, Destination destination, int delaySeconds) throws TeleporterException
	{
		MixinTeleport.get().teleport(teleporteeObject, destination, delaySeconds);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: MESSAGE
	// -------------------------------------------- //
	
	// MSG: All
	public static boolean msgAll(String msg)
	{
		return MixinMessage.get().msgAll(msg);
	}
	
	public static boolean msgAll(String msg, Object... args)
	{
		return MixinMessage.get().msgAll(msg, args);
	}
	
	public static boolean msgAll(Collection<String> msgs)
	{
		return MixinMessage.get().msgAll(msgs);
	}

	// MSG: Predicate
	public static boolean msgPredicate(Predicate<CommandSender> predicate, String msg)
	{
		return MixinMessage.get().msgPredicate(predicate, msg);
	}
	
	public static boolean msgPredicate(Predicate<CommandSender> predicate, String msg, Object... args)
	{
		return MixinMessage.get().msgPredicate(predicate, msg, args);
	}
	
	public static boolean msgPredicate(Predicate<CommandSender> predicate, Collection<String> msgs)
	{
		return MixinMessage.get().msgPredicate(predicate, msgs);
	}

	// MSG: One
	public static boolean msgOne(Object sendeeObject, String msg)
	{
		return MixinMessage.get().msgOne(sendeeObject, msg);
	}
	
	public static boolean msgOne(Object sendeeObject, String msg, Object... args)
	{
		return MixinMessage.get().msgOne(sendeeObject, msg, args);
	}
	
	public static boolean msgOne(Object sendeeObject, Collection<String> msgs)
	{
		return MixinMessage.get().msgOne(sendeeObject, msgs);
	}

	// MESSAGE: All
	public static boolean messageAll(Object message)
	{
		return MixinMessage.get().messageAll(message);
	}
	
	public static boolean messageAll(Object... messages)
	{
		return MixinMessage.get().messageAll(messages);
	}
	
	public static boolean messageAll(Collection<?> messages)
	{
		return MixinMessage.get().messageAll(messages);
	}

	// MESSAGE: Predicate
	public static boolean messagePredicate(Predicate<CommandSender> predicate, Object message)
	{
		return MixinMessage.get().messagePredicate(predicate, message);
	}
	
	public static boolean messagePredicate(Predicate<CommandSender> predicate, Object... messages)
	{
		return MixinMessage.get().messagePredicate(predicate, messages);
	}
	
	public static boolean messagePredicate(Predicate<CommandSender> predicate, Collection<?> messages)
	{
		return MixinMessage.get().messagePredicate(predicate, messages);
	}

	// MESSAGE: One
	public static boolean messageOne(Object sendeeObject, Object message)
	{
		return MixinMessage.get().messageOne(sendeeObject, message);
	}
	
	public static boolean messageOne(Object sendeeObject, Object... messages)
	{
		return MixinMessage.get().messageOne(sendeeObject, messages);
	}
	
	public static boolean messageOne(Object sendeeObject, Collection<?> messages)
	{
		return MixinMessage.get().messageOne(sendeeObject, messages);
	}

	// -------------------------------------------- //
	// STATIC EXPOSE: ACTIONBAR
	// -------------------------------------------- //

	public static boolean sendActionbarMessage(Object sendeeObject, String message)
	{
		return MixinActionbar.get().sendActionbarMessage(sendeeObject, message);
	}

	public static boolean sendActionbarMsg(Object sendeeObject, String message)
	{
		return MixinActionbar.get().sendActionbarMsg(sendeeObject, message);
	}

	public static boolean sendActionbarMson(Object sendeeObject, Mson mson)
	{
		return MixinActionbar.get().sendActionbarMson(sendeeObject, mson);
	}

	public static boolean isActionbarAvailable()
	{
		return MixinActionbar.get().isActionbarAvailable();
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: TITLE
	// -------------------------------------------- //
	
	public static boolean sendTitleMessage(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return MixinTitle.get().sendTitleMessage(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	public static boolean sendTitleMsg(Object watcherObject, int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return MixinTitle.get().sendTitleMsg(watcherObject, ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	public static boolean isTitlesAvailable()
	{
		return MixinTitle.get().isTitlesAvailable();
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: KICK
	// -------------------------------------------- //
	
	public static boolean kick(Object senderObject)
	{
		return MixinKick.get().kick(senderObject);
	}
	
	public static boolean kick(Object senderObject, String message)
	{
		return MixinKick.get().kick(senderObject, message);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: ACTUALL
	// -------------------------------------------- //
	
	public static boolean isActualJoin(PlayerJoinEvent event)
	{
		return MixinActual.get().isActualJoin(event);
	}
	
	public static boolean isActualLeave(EventMassiveCorePlayerLeave event)
	{
		return MixinActual.get().isActualLeave(event);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: COMMAND
	// -------------------------------------------- //
	
	public static boolean dispatchCommand(Object senderObject, String commandLine)
	{
		return MixinCommand.get().dispatchCommand(senderObject, commandLine);
	}
	
	public static boolean dispatchCommand(Object presentObject, Object senderObject, String commandLine)
	{
		return MixinCommand.get().dispatchCommand(presentObject, senderObject, commandLine);
	}
	
	// -------------------------------------------- //
	// STATIC EXPOSE: MODIFCATION
	// -------------------------------------------- //
	
	public static void syncModification(Entity<?> entity)
	{
		MixinModification.get().syncModification(entity);
	}
	
	public static void syncModification(Coll<?> coll, String id)
	{
		MixinModification.get().syncModification(coll, id);
	}

}
