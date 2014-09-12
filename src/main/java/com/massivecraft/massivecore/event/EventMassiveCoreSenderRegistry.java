package com.massivecraft.massivecore.event;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.util.IdData;

public abstract class EventMassiveCoreSenderRegistry extends EventMassiveCore
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	
	private final IdData data;
	public IdData getData() { return this.data; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreSenderRegistry(CommandSender sender, IdData data)
	{
		this.sender = sender;
		this.data = data;
	}
	
}
