package com.massivecraft.massivecore.cmd;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;

public class MassiveCommandException extends Exception
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	/*
	public MassiveCommandException()
	{
		
	}
	
	public MassiveCommandException(String msg)
	{
		this.messages.add(msg);
	}
	
	public MassiveCommandException(Collection<String> msgs)
	{
		this.messages.addAll(msgs);
	}
	*/
	
	// -------------------------------------------- //
	// MESSAGES
	// -------------------------------------------- //
	
	private List<String> messages = new MassiveList<String>();
	public List<String> getMessages() { return this.messages; }
	
	@Override
	public String getMessage()
	{
		return Txt.implode(this.getMessages(), "\n");
	}
	
	public MassiveCommandException setMessage(String message) { this.messages = new MassiveList<String>(message); return this; }
	public MassiveCommandException setMsg(String msg) { return this.setMessage(Txt.parse(msg)); }
	public MassiveCommandException setMsg(String msg, Object... args) { return this.setMessage(Txt.parse(msg, args)); }
	
	public MassiveCommandException addMessage(String message) { this.getMessages().add(message); return this; }
	public MassiveCommandException addMsg(String msg) { return this.addMessage(Txt.parse(msg)); }
	public MassiveCommandException addMsg(String msg, Object... args) { return this.addMessage(Txt.parse(msg, args)); }
	
	public MassiveCommandException setMessages(Collection<String> messages) { this.messages = new MassiveList<String>(messages); return this; }
	public MassiveCommandException setMessages(String... messages) { return this.setMessages(Arrays.asList(messages)); }
	public MassiveCommandException setMsgs(Collection<String> msgs) { return this.setMessages(Txt.parse(msgs)); }
	public MassiveCommandException setMsgs(String... msgs) { return this.setMsgs(Arrays.asList(msgs)); }
	
	public MassiveCommandException addMessages(Collection<String> messages) { this.getMessages().addAll(messages); return this; }
	public MassiveCommandException addMessages(String... messages) { return this.addMessages(Arrays.asList(messages)); }
	public MassiveCommandException addMsgs(Collection<String> messages) { this.getMessages().addAll(messages); return this; }
	public MassiveCommandException addMsgs(String... msgs) { return this.addMsgs(Arrays.asList(msgs)); }
	
}
