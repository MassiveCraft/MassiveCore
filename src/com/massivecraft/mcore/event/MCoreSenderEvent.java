package com.massivecraft.mcore.event;

import org.bukkit.command.CommandSender;

public abstract class MCoreSenderEvent extends MCoreEvent
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MCoreSenderEvent(CommandSender sender)
	{
		this.sender = sender;
	}
	
}
