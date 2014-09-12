package com.massivecraft.massivecore.cmd.req;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.cmd.MassiveCommand;

public class ReqAnd extends ReqAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static ReqAnd get(Req... reqs) { return new ReqAnd(reqs); }
	public ReqAnd(Req... reqs)
	{
		this(Arrays.asList(reqs));
	}
	
	public static ReqAnd get(Collection<Req> reqs) { return new ReqAnd(reqs); }
	public ReqAnd(Collection<Req> reqs)
	{
		this.reqs = Collections.unmodifiableList(new ArrayList<Req>(reqs));
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Req> reqs;
	public List<Req> getReqs() { return this.reqs; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender, MassiveCommand command)
	{
		return this.getFirstFailedSubreq(sender, command) == null;
	}
	
	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command)
	{
		return this.getFirstFailedSubreq(sender, command).createErrorMessage(sender, command);
	}
	
	public Req getFirstFailedSubreq(CommandSender sender, MassiveCommand command)
	{
		for (Req req : this.getReqs())
		{
			if (!req.apply(sender, command)) return req;
		}
		return null;
	}
	
}
