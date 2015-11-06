package com.massivecraft.massivecore.command.requirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.command.MassiveCommand;

public class RequirementAnd extends RequirementAbstract
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	public static RequirementAnd get(Requirement... requirements) { return new RequirementAnd(requirements); }
	public RequirementAnd(Requirement... requirements)
	{
		this(Arrays.asList(requirements));
	}
	
	public static RequirementAnd get(Collection<Requirement> requirements) { return new RequirementAnd(requirements); }
	public RequirementAnd(Collection<Requirement> requirements)
	{
		this.requirements = Collections.unmodifiableList(new ArrayList<Requirement>(requirements));
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final List<Requirement> requirements;
	public List<Requirement> getRequirements() { return this.requirements; }
	
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
	
	public Requirement getFirstFailedSubreq(CommandSender sender, MassiveCommand command)
	{
		for (Requirement requirement : this.getRequirements())
		{
			if (!requirement.apply(sender, command)) return requirement;
		}
		return null;
	}
	
}
