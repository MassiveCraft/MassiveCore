package com.massivecraft.mcore.event;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.util.IdData;

public abstract class MCoreSenderRegistryEvent extends MCoreEvent
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
	
	public MCoreSenderRegistryEvent(CommandSender sender, IdData data)
	{
		this.sender = sender;
		this.data = data;
	}
	
}
