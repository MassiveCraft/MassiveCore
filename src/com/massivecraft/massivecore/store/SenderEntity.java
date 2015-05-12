package com.massivecraft.massivecore.store;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.mixin.Mixin;
import com.massivecraft.massivecore.ps.PS;
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
	
	// Gets the object most suitable, when getting
	// this sender from IdUtil (most commonly done in mixins).
	public Object getSenderObject()
	{
		return this.getId();
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
		return Mixin.isOnline(this.getSenderObject());
	}
	
	public boolean isOffline()
	{
		return Mixin.isOffline(this.getSenderObject());
	}
	
	public Long getLastPlayed()
	{
		return Mixin.getLastPlayed(this.getSenderObject());
	}
	
	public Long getFirstPlayed()
	{
		return Mixin.getFirstPlayed(this.getSenderObject());
	}
	
	public boolean hasPlayedBefore()
	{
		return Mixin.hasPlayedBefore(this.getSenderObject());
	}
	
	// DISPLAY NAME
	
	@Deprecated
	public String getDisplayName()
	{
		return Mixin.getDisplayName(this.getSenderObject());
	}
	
	public String getDisplayName(Object watcherObject)
	{
		return Mixin.getDisplayName(this.getSenderObject(), watcherObject);
	}
	
	// CONVENIENCE SEND MESSAGE
	
	public boolean sendMessage(String message)
	{
		return Mixin.messageOne(this.getSenderObject(), message);
	}
	
	public boolean sendMessage(String... messages)
	{
		return Mixin.messageOne(this.getSenderObject(), messages);
	}
	
	public boolean sendMessage(Collection<String> messages)
	{
		return Mixin.messageOne(this.getSenderObject(), messages);
	}
	
	// CONVENIENCE MSG
	
	public boolean msg(String msg)
	{
		return Mixin.msgOne(this.getSenderObject(), msg);
	}
	
	public boolean msg(String msg, Object... args)
	{
		return Mixin.msgOne(this.getSenderObject(), msg, args);
	}
	
	public boolean msg(Collection<String> msgs)
	{
		return Mixin.msgOne(this.getSenderObject(), msgs);
	}
	
	// CONVENIENCE TITLE
	
	public boolean sendTitleMessage(int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return Mixin.sendTitleMessage(this.getSenderObject(), ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	public boolean sendTitleMsg(int ticksIn, int ticksStay, int ticksOut, String titleMain, String titleSub)
	{
		return Mixin.sendTitleMsg(this.getSenderObject(), ticksIn, ticksStay, ticksOut, titleMain, titleSub);
	}
	
	// CONVENIENCE GAME-MODE
	
	public GameMode getGameMode(GameMode def)
	{
		return IdUtil.getGameMode(this.getSenderObject(), def);
	}

	public boolean isGameMode(GameMode gm, boolean def)
	{
		return IdUtil.isGameMode(this.getSenderObject(), gm, def);
	}
	
	// COMVENIENCE DISPTACH COMMAND
	
	public boolean dispatchCommand(String commandLine)
	{
		return Mixin.dispatchCommand(this.getSenderObject(), commandLine);
	}
	
	public boolean dispatchCommand(Object presentObject, String commandLine)
	{
		return Mixin.dispatchCommand(presentObject, this.getSenderObject(), commandLine);
	}
	
	// CONVENIENCE PS
	
	public PS getPS()
	{
		return Mixin.getSenderPs(this.getSenderObject());
	}
	
	public void setPS(PS ps)
	{
		Mixin.setSenderPs(this.getSenderObject(), ps);
	}

}
