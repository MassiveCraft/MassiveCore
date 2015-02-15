package com.massivecraft.massivecore;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.Txt;


public class MassiveException extends Exception
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// MESSAGES
	// -------------------------------------------- //
	
	protected List<String> messages = new MassiveList<String>();
	public boolean hasMessages() { return ! this.messages.isEmpty(); }
	public List<String> getMessages() { return this.messages; }
	
	@Override
	public String getMessage()
	{
		return Txt.implode(this.getMessages(), "\n");
	}
	
	public MassiveException setMessage(String message) { this.messages = new MassiveList<String>(message); return this; }
	public MassiveException setMsg(String msg) { return this.setMessage(Txt.parse(msg)); }
	public MassiveException setMsg(String msg, Object... args) { return this.setMessage(Txt.parse(msg, args)); }
	
	public MassiveException addMessage(String message) { this.getMessages().add(message); return this; }
	public MassiveException addMsg(String msg) { return this.addMessage(Txt.parse(msg)); }
	public MassiveException addMsg(String msg, Object... args) { return this.addMessage(Txt.parse(msg, args)); }
	
	public MassiveException setMessages(Collection<String> messages) { this.messages = new MassiveList<String>(messages); return this; }
	public MassiveException setMessages(String... messages) { return this.setMessages(Arrays.asList(messages)); }
	public MassiveException setMsgs(Collection<String> msgs) { return this.setMessages(Txt.parse(msgs)); }
	public MassiveException setMsgs(String... msgs) { return this.setMsgs(Arrays.asList(msgs)); }
	
	public MassiveException addMessages(Collection<String> messages) { this.getMessages().addAll(messages); return this; }
	public MassiveException addMessages(String... messages) { return this.addMessages(Arrays.asList(messages)); }
	public MassiveException addMsgs(Collection<String> messages) { this.getMessages().addAll(messages); return this; }
	public MassiveException addMsgs(String... msgs) { return this.addMsgs(Arrays.asList(msgs)); }
	
}
