package com.massivecraft.massivecore.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MassiveCommandException extends RuntimeException
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private List<String> errorMsgs  = new ArrayList<String>();
	public List<String> getErrorMsgs() { return this.errorMsgs; }
	public void setErrorMsgs(List<String> msgs) { this.errorMsgs = msgs; }
	public void addErrorMsg(String msg) { this.errorMsgs.add(msg); }
	
	// -------------------------------------------- //
	// CONSTRUCTORS
	// -------------------------------------------- //
	
	public MassiveCommandException()
	{

	}
	
	public MassiveCommandException(String msg)
	{
		this.errorMsgs.add(msg);
	}
	
	public MassiveCommandException(Collection<String> msgs)
	{
		this.errorMsgs.addAll(msgs);
	}
	
}
