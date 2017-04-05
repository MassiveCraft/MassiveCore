package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.command.PlayerValue;
import org.bukkit.event.HandlerList;

import java.io.Serializable;

public class EventMassiveCoreCommandSet<T extends Serializable> extends EventMassiveCore implements Serializable
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	private final String senderId;
	public String getSenderId() { return senderId; }
	
	private final String targetId;
	public String getTargetId() { return targetId; }
	
	private final T value;
	public T getValue() { return value; }
	
	private final PlayerValue<T> playerValue;
	public PlayerValue<T> getPlayerValue() { return playerValue; }
	
	private final String name;
	public String getName() { return name; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreCommandSet(String senderId, String targetId, T value, PlayerValue<T> playerValue, String name)
	{
		this.senderId = senderId;
		this.targetId = targetId;
		this.value = value;
		this.playerValue = playerValue;
		this.name = name;
	}
	
}
