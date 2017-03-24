package com.massivecraft.massivecore;

import com.massivecraft.massivecore.mson.Mson;

import java.util.Arrays;
import java.util.Collection;


public class MassiveException extends Exception
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// MESSAGES
	// -------------------------------------------- //
	
	protected Mson messages = Mson.mson();
	public boolean hasMessages() { return ! this.messages.isEmpty(); }
	public Mson getMessages() { return this.messages; }
	
	@Override
	public String getMessage()
	{
		return this.messages.toPlain(true);
	}
	
	// Set single
	public MassiveException setMessage(Object part) { this.messages = Mson.mson(part); return this; }
	public MassiveException setMsg(String msg) { this.messages = Mson.parse(msg); return this; }
	public MassiveException setMsg(String msg, Object... objects) { this.messages = Mson.parse(msg, objects); return this; }
	
	// Add single
	public MassiveException addMessage(Object part)
	{
		// Only add a newline if not empty.
		Mson mson = this.messages.isEmpty() ? Mson.mson(part) : Mson.mson("\n", part);
		this.messages = this.messages.add(mson);
		return this;
	}
	public MassiveException addMsg(String msg) { return this.addMessage(Mson.parse(msg)); }
	public MassiveException addMsg(String msg, Object... args) { return this.addMessage(Mson.parse(msg, args)); }
	
	// Set several
	public MassiveException setMsgs(Collection<String> msgs) { this.messages = Mson.parse(msgs); return this; }
	public MassiveException setMsgs(String... msgs) { return this.setMsgs(Arrays.asList(msgs)); }
	
	public MassiveException addMsgs(Collection<String> msgs) { return this.addMessage(Mson.parse(msgs)); }
	public MassiveException addMsgs(String... msgs) { return this.addMsgs(Arrays.asList(msgs)); }
	
}
