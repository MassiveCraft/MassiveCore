package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Objects;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.PlayerState;
import com.massivecraft.massivecore.event.EventMassiveCoreAknowledge;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mixin.MixinPlayed;
import com.massivecraft.massivecore.mixin.MixinVisibility;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.PermissionUtil;

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
	public <T> T convertGet(T value, T standard, Object permission)
	{
		// Create
		T ret = this.convertGet(value, standard);
		
		// Permission Requirement
		if ( ! Objects.equal(value, standard) && !this.hasPermission(permission, true)) return standard;
		
		// Return
		return ret;
	}
	
	// BOOLEAN
	public boolean convertGet(Boolean value, Object permission)
	{
		return this.convertGet(value, false, permission);
	}
	
	// -------------------------------------------- //
	// CONVENIENCE: PERMISSON
	// -------------------------------------------- //
	
	public Boolean hasPermission(Object permission, Boolean unsure, boolean verboose)
	{
		// Null
		if (permission == null) throw new NullPointerException("permission");
		
		// Sender
		CommandSender sender = this.getSender();
		if (sender == null) return unsure;
		
		// Players must be fully joined.
		// The permission manager may not have updated permissions during the early login/connect stages. 
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			PlayerState state = PlayerState.get(player);
			if (state != PlayerState.JOINED) return unsure;
		}
		
		// Check
		return PermissionUtil.hasPermission(sender, permission, verboose);
	}
	
	public Boolean hasPermission(Object permission, Boolean unsure)
	{
		return this.hasPermission(permission, unsure, false);
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
		return MixinPlayed.get().isOnline(this.getId());
	}
	
	public boolean isOffline()
	{
		return MixinPlayed.get().isOffline(this.getId());
	}
	
	public Long getLastPlayed()
	{
		return MixinPlayed.get().getLastPlayed(this.getId());
	}
	
	public Long getFirstPlayed()
	{
		return MixinPlayed.get().getFirstPlayed(this.getId());
	}
	
	public boolean hasPlayedBefore()
	{
		return MixinPlayed.get().hasPlayedBefore(this.getId());
	}
	
	public String getIp()
	{
		return MixinPlayed.get().getIp(this.getId());
	}
	
	public boolean isVisible()
	{
		return MixinVisibility.get().isVisible(this);
	}
	
	public boolean isVisible(Object watcherObject)
	{
		return MixinVisibility.get().isVisible(this, watcherObject);
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
	
	public String getDisplayName(Object watcherObject)
	{
		return MixinDisplayName.get().getDisplayName(this.getId(), watcherObject);
	}
	
	public Mson getDisplayNameMson(Object watcherObject)
	{
		return MixinDisplayName.get().getDisplayNameMson(this.getId(), watcherObject);
	}
	
	// -------------------------------------------- //
	// AKNOWLEDGE
	// -------------------------------------------- //
	
	public boolean isAcknowledging(Object sender)
	{
		EventMassiveCoreAknowledge event = new EventMassiveCoreAknowledge(sender, this);
		event.run();
		return ! event.isCancelled();
	}
	
	// -------------------------------------------- //
	// MSG / MESSAGE
	// -------------------------------------------- //
	
	// MSG
	
	public boolean msg(String msg)
	{
		return MixinMessage.get().msgOne(this.getId(), msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return MixinMessage.get().msgOne(this.getId(), msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return MixinMessage.get().msgOne(this.getId(), msgs);
	}
	
	// MESSAGE
	
	public boolean message(Object message)
	{
		return MixinMessage.get().messageOne(this.getId(), message);
	}
	
	public boolean message(Object... messages)
	{
		return MixinMessage.get().messageOne(this.getId(), messages);
	}
	
	public boolean message(Collection<?> messages)
	{
		return MixinMessage.get().messageOne(this.getId(), messages);
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
