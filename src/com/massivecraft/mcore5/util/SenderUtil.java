package com.massivecraft.mcore5.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.server.v1_4_R1.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.mixin.Mixin;
import com.massivecraft.mcore5.sender.FakeBlockCommandSender;
import com.massivecraft.mcore5.store.SenderColl;

/**
 * This Util was created to fill out the void between the Player interface and other CommandSenders.
 * 
 * +++ The ID +++
 * We add an ID <--> CommandSender lookup feature.
 * Each player has an id which is the name of the player. Players are retrievable by id using Bukkit.getPlayerExact().
 * Other command senders have no true id. We make it so they have.
 * Non-player-sender-ids always start with and ampersand (@). This is to avoid clashes with regular player names.
 * The id is simply "@"+CommandSender.getName() with exception for the block command sender which we call "@block".
 * Non standard CommandSenders must be manually registered to the util using the register method.
 * 
 * +++ The DisplayName and ListName +++
 * CommandSenders can have DisplayName and ListName just like normal Player.
 * 
 * +++ Online/Offline  +++
 * Players may be Online/Offline. We allow CommandSenders to be Online/Offline as well.
 * This is simply done by stating that everything non-player in online all the time.  
 * The ConsoleCommandSender is for example always online and never offline.
 * 
 * +++ Easy sendMessage and dispatchCommand +++
 * This feature isn't new "fake fields" like the ones above.
 * Its a suite of useful utility methods for sending messages and dispatching commands as a certain sender.
 * 
 */
public class SenderUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	// The id prefix
	public final static String IDPREFIX = "@";
	
	// Ids for standard-non-players
	public final static String ID_CONSOLE = IDPREFIX+"console";
	public final static String ID_RCON = IDPREFIX+"rcon";
	public final static String ID_BLOCK = IDPREFIX+"block";
	
	// Names for standard-non-players
	public final static String VANILLA_CONSOLE_NAME = "CONSOLE";
	public final static String VANILLA_RCON_NAME = "Rcon";
	public final static String VANILLA_BLOCK_NAME = "@";
	
	// -------------------------------------------- //
	// REGISTRY
	// -------------------------------------------- //
	
	protected static Map<String, CommandSender> idToSender = new TreeMap<String, CommandSender>(String.CASE_INSENSITIVE_ORDER);
	protected static Map<String, String> idToListName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	
	public static synchronized boolean register(CommandSender sender)
	{
		if (sender == null) return false;
		String id = getSenderId(sender);
		CommandSender current = idToSender.get(id);
		if (current != null) return current == sender;
		idToSender.put(id, sender);
		SenderColl.setSenderRefferences(id, sender);
		return true;
	}
	
	public static synchronized boolean unregister(CommandSender sender)
	{
		return idToSender.remove(getSenderId(sender)) != null;
	}
	
	public static Map<String, CommandSender> getIdToSender()
	{
		return Collections.unmodifiableMap(idToSender);
	}
	
	static
	{
		// Register
		register(getConsole());
		register(getRcon());
		register(getBlock());
		
		// Display Name
		setDisplayName(ID_CONSOLE, ChatColor.RED.toString()+ID_CONSOLE.toUpperCase());
		setDisplayName(ID_RCON, ChatColor.RED.toString()+ID_RCON.toUpperCase());
		setDisplayName(ID_BLOCK, ChatColor.RED.toString()+ID_BLOCK.toUpperCase());
		
		// List Name
		setListName(ID_CONSOLE, ChatColor.RED.toString()+ID_CONSOLE.toUpperCase());
		setListName(ID_RCON, ChatColor.RED.toString()+ID_RCON.toUpperCase());
		setListName(ID_BLOCK, ChatColor.RED.toString()+ID_BLOCK.toUpperCase());
	}
	
	// -------------------------------------------- //
	// ID TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isSenderId(Object o)
	{
		return o instanceof String;
	}
	
	public static boolean isPlayerId(Object o)
	{
		return MUtil.isValidPlayerName(o);
	}
	
	public static boolean isConsoleId(Object o)
	{
		return ID_CONSOLE.equals(o);
	}
	
	public static boolean isRconId(Object o)
	{
		return ID_RCON.equals(o);
	}
	
	public static boolean isBlockId(Object o)
	{
		return ID_BLOCK.equals(o);
	}
	
	public static boolean isNonplayerId(Object o)
	{
		if (!isSenderId(o)) return false;
		if (isPlayerId(o)) return false;
		return true;
	}
	
	public static boolean isStandardNonplayerId(Object o)
	{
		if (isConsoleId(o)) return true;
		if (isRconId(o)) return true;
		if (isBlockId(o)) return true;
		return false;
	}
	
	public static boolean isNonstandardNonplayerId(Object o)
	{
		if (!isSenderId(o)) return false;
		if (isStandardNonplayerId(o)) return false;
		if (isPlayerId(o)) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// SENDER/OBJECT TYPE CHECKING
	// -------------------------------------------- //
	
	public static boolean isSender(Object o)
	{
		// The object must be a CommandSender and musn't be null.
		return o instanceof CommandSender;
	}
	
	public static boolean isPlayer(Object o)
	{
		return o instanceof Player;
	}
	
	public static boolean isConsole(Object o)
	{
		if (!(o instanceof ConsoleCommandSender)) return false;
		if (!VANILLA_CONSOLE_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isRcon(Object o)
	{
		if (!(o instanceof RemoteConsoleCommandSender)) return false;
		if (!VANILLA_RCON_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isBlock(Object o)
	{
		if (!(o instanceof BlockCommandSender)) return false;
		if (!VANILLA_BLOCK_NAME.equals(((CommandSender)o).getName())) return false;
		return true;
	}
	
	public static boolean isNonplayer(Object o)
	{
		if (!isSender(o)) return false;
		if (isPlayer(o)) return false;
		return true;
	}
	
	public static boolean isStandardNonplayer(Object o)
	{
		if (isConsole(o)) return true;
		if (isRcon(o)) return true;
		if (isBlock(o)) return true;
		return false;
	}
	
	public static boolean isNonstandardNonplayer(Object o)
	{
		if (!isSender(o)) return false;
		if (isStandardNonplayer(o)) return false;
		if (isPlayer(o)) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// GET ID
	// -------------------------------------------- //
	
	public static String getSenderId(Object o)
	{
		if (!isSender(o)) return null;
		if (isPlayer(o)) return ((CommandSender)o).getName();
		if (isConsole(o)) return ID_CONSOLE;
		if (isRcon(o)) return ID_RCON;
		if (isBlock(o)) return ID_BLOCK;
		return IDPREFIX+((CommandSender)o).getName();
	}
	
	// -------------------------------------------- //
	// GET SENDER
	// -------------------------------------------- //
	
	// ACTUALL LOGIC
	
	public static synchronized CommandSender getSender(String senderId)
	{
		if (senderId == null) return null;
		if (isPlayerId(senderId))
		{
			return Bukkit.getPlayerExact(senderId);
		}
		return idToSender.get(senderId);
	}
	
	// ID STUFF

	public static Player getPlayer(String senderId)
	{
		return getAsPlayer(getSender(senderId));
	}
	
	public static ConsoleCommandSender getConsole(String senderId)
	{
		return getAsConsole(getSender(senderId));
	}
	
	public static RemoteConsoleCommandSender getRcon(String senderId)
	{
		return getAsRcon(getSender(senderId));
	}
	
	public static BlockCommandSender getBlock(String senderId)
	{
		return getAsBlock(getSender(senderId));
	}
	
	// MARCHAL STUFF
	
	public static CommandSender getAsSender(Object o)
	{
		if (!isSender(o)) return null;
		return (CommandSender) o;
	}
	
	public static Player getAsPlayer(Object o)
	{
		if (!isPlayer(o)) return null;
		return (Player) o;
	}
	
	public static ConsoleCommandSender getAsConsole(Object o)
	{
		if (!isConsole(o)) return null;
		return (ConsoleCommandSender) o;
	}
	
	public static RemoteConsoleCommandSender getAsRcon(Object o)
	{
		if (!isRcon(o)) return null;
		return (RemoteConsoleCommandSender) o;
	}
	
	public static BlockCommandSender getAsBlock(Object o)
	{
		if (!isBlock(o)) return null;
		return (BlockCommandSender) o;
	}
	
	// -------------------------------------------- //
	// GET STANDARD-NON-PLAYERS
	// -------------------------------------------- //
	
	public static ConsoleCommandSender getConsole()
	{
		return Bukkit.getConsoleSender();
	}
	
	public static RemoteConsoleCommandSender getRcon()
	{
		Server server = Bukkit.getServer();
		CraftServer craftServer = (CraftServer)server;
		MinecraftServer minecraftServer = craftServer.getServer();
		return minecraftServer.remoteConsole;
	}
	
	public static BlockCommandSender getBlock()
	{
		return FakeBlockCommandSender.get();
	}
	
	// -------------------------------------------- //
	// ONLINE/OFFLINE
	// -------------------------------------------- //
	// What about visibility? And the hide player API?
	
	public static boolean isOnline(String senderId)
	{
		if (senderId == null) return false;
		if (isPlayerId(senderId))
		{
			Player player = Bukkit.getPlayer(senderId);
			if (player == null) return false;
			return player.isOnline();
		}
		else
		{
			// Non-players must be registered for us to consider them online.
			CommandSender sender = getSender(senderId);
			return sender != null;
		}
	}
	
	public static boolean isOffline(String senderId)
	{
		return ! isOnline(senderId);
	}
	
	public static boolean isOnline(CommandSender sender)
	{
		return isOnline(getSenderId(sender));
	}
	
	public static boolean isOffline(CommandSender sender)
	{
		return isOffline(getSenderId(sender));
	}
	
	// -------------------------------------------- //
	// GET ALL ONLINE
	// -------------------------------------------- //
	
	public static List<CommandSender> getOnlineSenders()
	{
		List<CommandSender> ret = new ArrayList<CommandSender>(Arrays.asList(Bukkit.getOnlinePlayers()));
		for (Entry<String, CommandSender> entry : idToSender.entrySet())
		{
			String id = entry.getKey();
			CommandSender sender = entry.getValue();
			if (isPlayerId(id)) continue;
			ret.add(sender);
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// DISPLAY NAME
	// -------------------------------------------- //
	
	public static String getDisplayName(String senderId)
	{
		return Mixin.getDisplayNameMixin().get(senderId);
	}
	
	public static void setDisplayName(String senderId, String displayName)
	{
		Mixin.getDisplayNameMixin().set(senderId, displayName);
	}
	
	public static String getDisplayName(CommandSender sender)
	{
		return Mixin.getDisplayNameMixin().get(sender);
	}
	
	public static void setDisplayName(CommandSender sender, String displayName)
	{
		Mixin.getDisplayNameMixin().set(sender, displayName);
	}
	
	// -------------------------------------------- //
	// LIST NAME
	// -------------------------------------------- //
	
	public static String getListName(String senderId)
	{
		return Mixin.getListNameMixin().get(senderId);
	}
	
	public static void setListName(String senderId, String displayName)
	{
		Mixin.getListNameMixin().set(senderId, displayName);
	}
	
	public static String getListName(CommandSender sender)
	{
		return Mixin.getListNameMixin().get(sender);
	}
	
	public static void setListName(CommandSender sender, String displayName)
	{
		Mixin.getListNameMixin().set(sender, displayName);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE CMD
	// -------------------------------------------- //
	
	public static boolean cmd(CommandSender sender, String cmd)
	{
		return Bukkit.dispatchCommand(sender, cmd);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE SEND MESSAGE
	// -------------------------------------------- //
	
	// sender
	
	public static boolean sendMessage(CommandSender sender, String message)
	{
		if (sender == null) return false;
		sender.sendMessage(message);
		return true;
	}
	
	public static boolean sendMessage(CommandSender sender, String... messages)
	{
		if (sender == null) return false;
		sender.sendMessage(messages);
		return true;
	}
	
	public static boolean sendMessage(CommandSender sender, Collection<String> messages)
	{
		if (sender == null) return false;
		for (String message : messages)
		{
			sender.sendMessage(message);
		}
		return true;
	}
	
	// senderId
	
	public static boolean sendMessage(String senderId, String message)
	{
		return sendMessage(getSender(senderId), message);
	}
	
	public static boolean sendMessage(String senderId, String... messages)
	{
		return sendMessage(getSender(senderId), messages);
	}
	
	public static boolean sendMessage(String senderId, Collection<String> messages)
	{
		return sendMessage(getSender(senderId), messages);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE MSG
	// -------------------------------------------- //
	
	// sender
	
	public static boolean msg(CommandSender sender, String msg)
	{
		return sendMessage(sender, Txt.parse(msg));
	}
	
	public static boolean msg(CommandSender sender, String msg, Object... args)
	{
		return sendMessage(sender, Txt.parse(msg, args));
	}
	
	public static boolean msg(CommandSender sender, Collection<String> msgs)
	{
		if (sender == null) return false;
		for (String msg : msgs)
		{
			msg(sender, msg);
		}
		return true;
	}
	
	// senderId
	
	public static boolean msg(String senderId, String msg)
	{
		return msg(getSender(senderId), msg);
	}
	
	public static boolean msg(String senderId, String msg, Object... args)
	{
		return msg(getSender(senderId), msg, args);
	}
	
	public static boolean msg(String senderId, Collection<String> msgs)
	{
		return msg(getSender(senderId), msgs);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE GAME-MODE
	// -------------------------------------------- //
	
	public static GameMode getGameMode(String senderId, GameMode def)
	{
		Player player = getPlayer(senderId);
		if (player == null) return def;
		return player.getGameMode();
	}

	public static boolean isGameMode(String senderId, GameMode gm, boolean def)
	{
		Player player = getPlayer(senderId);
		if (player == null) return def;
		return player.getGameMode() == gm;
	}
	
	public static GameMode getGameMode(CommandSender sender, GameMode def)
	{
		return getGameMode(getSenderId(sender), def);
	}

	public static boolean isGameMode(CommandSender sender, GameMode gm, boolean def)
	{
		return isGameMode(getSenderId(sender), gm, def);
	}
	
}
