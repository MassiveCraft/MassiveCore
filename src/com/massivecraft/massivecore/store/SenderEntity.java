package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.PermUtil;

public abstract class SenderEntity<E extends SenderEntity<E>> extends Entity<E> implements Named
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

	@Override public String getName()
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
	// CONVENIENCE: DATABASE
	// -------------------------------------------- //
	
	// GENERIC
	public <T> T convertGet(T value, T defaultValue, String permission)
	{
		// Create
		T ret = super.convertGet(value, defaultValue);
		
		// Permission Requirement
		if ( ! Objects.equal(value, defaultValue) && ! PermUtil.has(this.getSender(), permission)) return defaultValue;
		
		// Return
		return ret;
	}
	
	// BOOLEAN
	public boolean convertGet(Boolean value, String permission)
	{
		return this.convertGet(value, false, permission);
	}
	
	// -------------------------------------------- //
	// TYPE
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
	
	// -------------------------------------------- //
	// ONLINE / OFFLINE
	// -------------------------------------------- //
	// TODO: Update with new presence enumeration?

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
	
	public String getIp()
	{
		return Mixin.getIp(this.getId());
	}
	
	public boolean isVisible()
	{
		return Mixin.isVisible(this);
	}
	
	public boolean isVisible(Object watcherObject)
	{
		return Mixin.isVisible(this, watcherObject);
	}
	
	public boolean isOnline(Object watcherObject)
	{
		return this.isOnline() && this.isVisible(watcherObject);
	}
	
	public boolean isOffline(Object watcherObject)
	{
		return ! this.isOnline(watcherObject);
	}
	
	// -------------------------------------------- //
	// DISPLAY NAME
	// -------------------------------------------- //
	
	// TODO: Remove this one shortly.
	@Deprecated
	public String getDisplayName()
	{
		return Mixin.getDisplayName(this.getId());
	}
	
	public String getDisplayName(Object watcherObject)
	{
		return Mixin.getDisplayName(this.getId(), watcherObject);
	}
	
	public Mson getDisplayNameMson(Object watcherObject)
	{
		return Mixin.getDisplayNameMson(this.getId(), watcherObject);
	}
	
	// -------------------------------------------- //
	// MSG / MESSAGE
	// -------------------------------------------- //
	
	// MSG
	
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
	
	// MESSAGE
	
	public boolean message(Object message)
	{
		return Mixin.messageOne(this.getId(), message);
	}
	
	public boolean message(Object... messages)
	{
		return Mixin.messageOne(this.getId(), messages);
	}
	
	public boolean message(Collection<?> messages)
	{
		return Mixin.messageOne(this.getId(), messages);
	}
	
	// -------------------------------------------- //
	// GAME-MODE
	// -------------------------------------------- //
	
	public GameMode getGameMode(GameMode def)
	{
		return IdUtil.getGameMode(this.getId(), def);
	}

	public boolean isGameMode(GameMode gm, boolean def)
	{
		return IdUtil.isGameMode(this.getId(), gm, def);
	}
	
}
