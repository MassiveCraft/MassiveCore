package com.massivecraft.mcore5.store;

import java.util.Collection;

import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.mcore5.mixin.Mixin;
import com.massivecraft.mcore5.util.SenderUtil;

public abstract class SenderEntity<E extends SenderEntity<E>> extends Entity<E, String>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// We keep a reference to the sender to provide fast "reverse lookup".
	// The sender reference is initiated here and is kept updated using the InternalListener
	protected transient CommandSender sender = null;
	protected transient boolean senderInitiated = false;
	public CommandSender getSender()
	{
		if ( ! this.senderInitiated)
		{
			this.sender = SenderUtil.getSender(this.getId());
			this.senderInitiated = true;
		}
		return this.sender;
	}
	
	// -------------------------------------------- //
	// FIXED ID
	// -------------------------------------------- //
	
	public String getFixedId()
	{
		return Mixin.tryFix(this.getId());
	}
	
	// -------------------------------------------- //
	// OVERRIDES
	// -------------------------------------------- //
	
	@Override
	public SenderColl<E> getColl()
	{
		return (SenderColl<E>) super.getColl();
	}
	
	// -------------------------------------------- //
	// SENDER UTIL METHOD MIRRORING
	// -------------------------------------------- //
	
	// IS
	
	public boolean isSender()
	{
		return SenderUtil.isSenderId(this.getId());
	}
	
	public boolean isPlayer()
	{
		return SenderUtil.isPlayerId(this.getId());
	}
	
	public boolean isConsole()
	{
		return SenderUtil.isConsoleId(this.getId());
	}
	
	public boolean isRcon()
	{
		return SenderUtil.isRconId(this.getId());
	}
	
	public boolean isBlock()
	{
		return SenderUtil.isBlockId(this.getId());
	}
	
	public boolean isNonplayer()
	{
		return SenderUtil.isNonplayerId(this.getId());
	}
	
	public boolean isStandardNonplayer()
	{
		return SenderUtil.isStandardNonplayerId(this.getId());
	}
	
	public boolean isNonstandardNonplayer()
	{
		return SenderUtil.isNonstandardNonplayerId(this.getId());
	}
	
	// GET
	
	// TODO: Usage of sender instead of id here is cheating but is good performance-wise so it can be ok.
	
	public Player getPlayer()
	{
		return SenderUtil.getAsPlayer(this.getSender());
	}
	
	public ConsoleCommandSender getConsole()
	{
		return SenderUtil.getAsConsole(this.getSender());
	}
	
	public RemoteConsoleCommandSender getRcon()
	{
		return SenderUtil.getAsRcon(this.getSender());
	}
	
	public BlockCommandSender getBlock()
	{
		return SenderUtil.getAsBlock(this.getSender());
	}
	
	// ONLINE / OFFLINE
	
	public boolean isOnline()
	{
		return Mixin.isOnline(this.getId());
	}
	
	public boolean isOffline()
	{
		return Mixin.isOffline(this.getId());
	}
	
	// DISPLAY NAME
	
	public String getDisplayName()
	{
		return Mixin.getDisplayName(this.getId());
	}
	
	public void setDisplayName(String displayName)
	{
		Mixin.setDisplayName(this.getId(), displayName);
	}
	
	// LIST NAME
	
	public String getListName()
	{
		return Mixin.getListName(this.getId());
	}
	
	public void setListName(String listName)
	{
		Mixin.setListName(this.getId(), listName);
	}
	
	// CONVENIENCE SEND MESSAGE
	
	public boolean sendMessage(String message)
	{
		return Mixin.message(this.getId(), message);
	}
	
	public boolean sendMessage(String... messages)
	{
		return Mixin.message(this.getId(), messages);
	}
	
	public boolean sendMessage(Collection<String> messages)
	{
		return Mixin.message(this.getId(), messages);
	}
	
	// CONVENIENCE MSG
	
	public boolean msg(String msg)
	{
		return Mixin.msg(this.getId(), msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return Mixin.msg(this.getId(), msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return Mixin.msg(this.getId(), msgs);
	}
	
	// CONVENIENCE GAME-MODE
	
	public GameMode getGameMode(GameMode def)
	{
		return SenderUtil.getGameMode(this.getId(), def);
	}

	public boolean isGameMode(GameMode gm, boolean def)
	{
		return SenderUtil.isGameMode(this.getId(), gm, def);
	}
	
}
