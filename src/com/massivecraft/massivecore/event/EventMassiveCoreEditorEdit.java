package com.massivecraft.massivecore.event;

import com.massivecraft.massivecore.command.editor.CommandEditAbstract;
import org.bukkit.event.HandlerList;

public class EventMassiveCoreEditorEdit<O, V> extends EventMassiveCore
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
	
	protected final CommandEditAbstract<O, V> command;
	public CommandEditAbstract<O, V> getCommand() { return this.command; }
	
	protected final O source;
	public O getSource() { return this.source; }
	
	protected final V before;
	public V getBefore() { return this.before; }
	
	protected V after;
	public V getAfter() { return this.after; }
	public void setAfter(V after) { this.after = after; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMassiveCoreEditorEdit(CommandEditAbstract<O, V> command, O source, V before, V after)
	{
		this.command = command;
		this.source = source;
		this.before = before;
		this.after = after;
	}
	
}
