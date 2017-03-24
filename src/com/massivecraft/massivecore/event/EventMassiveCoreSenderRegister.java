package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.util.IdData;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventMassiveCoreSenderRegister extends EventMassiveCoreSenderRegistry
{	
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreSenderRegister(CommandSender sender, IdData data)
	{
		super(sender, data);
	}
	
}
