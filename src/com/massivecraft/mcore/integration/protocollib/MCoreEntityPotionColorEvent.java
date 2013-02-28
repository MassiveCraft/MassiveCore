package com.massivecraft.mcore.integration.protocollib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCoreEntityPotionColorEvent extends Event implements Runnable
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS & RAWDATA GET/SET
	// -------------------------------------------- //
	
	private final Player sendee;
	public Player getSendee() { return this.sendee; }
	
	private final Entity entity;
	public Entity getEntity() { return this.entity; }
	
	// http://www.wiki.vg/Entities#Index_8.2C_int:_Potion_effects
	private int color;
	public int getColor() { return this.color; }
	public void setColor(int color) { this.color = color; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public MCoreEntityPotionColorEvent(Player sendee, Entity entity, int color)
	{
		this.sendee = sendee;
		this.entity = entity;
		this.color = color;
	}
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		Bukkit.getPluginManager().callEvent(this);
	}
	
}
