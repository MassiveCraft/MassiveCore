package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.util.IdUtil;

public abstract class SenderEntity<E extends SenderEntity<E>> extends Entity<E>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// TODO: With the new IdUtil containing a built in index ;
	// TODO: ; Do we even need this local-cache?
	// We keep a reference to the sender to provide fast "reverse lookup".
	// The sender reference is initiated here and is kept updated using the InternalListener
	protected transient CommandSender sender = null;
	protected transient boolean senderInitiated = false;
	public CommandSender getSender()
	{
		if ( ! this.senderInitiated) this.initiateSender();
		return this.sender;
	}
	
	public void initiateSender()
	{
		String id = this.getId();
		if (id == null) return;
		this.sender = IdUtil.getSender(id);
		this.senderInitiated = true;
	}

	public String getName()
	{
		return IdUtil.getName(this.getId());
	}
	
	public UUID getUuid()
	{
		return IdUtil.getUuid(this.getId());
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
	
	public boolean isPlayer()
	{
		return IdUtil.isPlayerId(this.getId());
	}
	
	public boolean isConsole()
	{
		return IdUtil.isConsoleId(this.getId());
	}
	
	// GET
	
	public Player getPlayer()
	{
		return IdUtil.getAsPlayer(this.getSender());
	}
	
	public ConsoleCommandSender getConsole()
	{
		return IdUtil.getAsConsole(this.getSender());
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
	
	public Long getLastPlayed()
	{
		return Mixin.getLastPlayed(this.getId());
	}
	
	public Long getFirstPlayed()
	{
		return Mixin.getFirstPlayed(this.getId());
	}
	
	public boolean hasPlayedBefore()
	{
		return Mixin.hasPlayedBefore(this.getId());
	}
	
	// DISPLAY NAME
	
	@Deprecated
	public String getDisplayName()
	{
		return Mixin.getDisplayName(this.getId());
	}
	
	public String getDisplayName(Object watcherObject)
	{
		return Mixin.getDisplayName(this.getId(), watcherObject);
	}
	
	// CONVENIENCE SEND MESSAGE
	
	public boolean sendMessage(String message)
	{
		return Mixin.messageOne(this.getId(), message);
	}
	
	public boolean sendMessage(String... messages)
	{
		return Mixin.messageOne(this.getId(), messages);
	}
	
	public boolean sendMessage(Collection<String> messages)
	{
		return Mixin.messageOne(this.getId(), messages);
	}
	
	// CONVENIENCE MSG
	
	public boolean msg(String msg)
	{
		return Mixin.msgOne(this.getId(), msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return Mixin.msgOne(this.getId(), msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return Mixin.msgOne(this.getId(), msgs);
	}
	
	// CONVENIENCE GAME-MODE
	
	public GameMode getGameMode(GameMode def)
	{
		return IdUtil.getGameMode(this.getId(), def);
	}

	public boolean isGameMode(GameMode gm, boolean def)
	{
		return IdUtil.isGameMode(this.getId(), gm, def);
	}
	
}
