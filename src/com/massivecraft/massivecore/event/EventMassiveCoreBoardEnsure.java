package com.massivecraft.massivecore.event;

import org.bukkit.event.HandlerList;

public class EventMassiveCoreBoardEnsure extends EventMassiveCore
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
	
	private boolean ensureBoardEnabled = false;
	public boolean isEnsureBoardEnabled() { return this.ensureBoardEnabled; }
	public void setEnsureBoardEnabled() { this.ensureBoardEnabled = true; }
	
	private boolean ensureBoardStrict = false;
	public boolean isEnsureBoardStrict() { return this.ensureBoardStrict; }
	public void setEnsureBoardStrict() { this.ensureBoardStrict = true; }
	
	private boolean ensureTeamEnabled = false;
	public boolean isEnsureTeamEnabled() { return this.ensureTeamEnabled; }
	public void setEnsureTeamEnabled() { this.ensureTeamEnabled = true; }
	
	private boolean ensureTeamStrict = false;
	public boolean isEnsureTeamStrict() { return this.ensureTeamStrict; }
	public void setEnsureTeamStrict() { this.ensureTeamStrict = true; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreBoardEnsure()
	{
		
	}
	
}
