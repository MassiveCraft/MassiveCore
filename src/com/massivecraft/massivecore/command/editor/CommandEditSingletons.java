package com.massivecraft.massivecore.command.editor;

import java.util.Collection;
import java.util.List;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.RegistryType;

public abstract class CommandEditSingletons<O> extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String permission;
	public String getPermission() { return this.permission; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CommandEditSingletons(Class<O> clazz, String permission)
	{
		// Aliases
		String name = RegistryType.getType(clazz).getName() + "s";
		this.setAliases(name);
		this.setDesc("edit " + name);
		
		// Requirements
		this.permission = permission;
		this.addRequirements(RequirementHasPerm.get(permission));
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract Collection<O> getAll();
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	// This is evaluated every time, because it might change.
	// In Factions we use this to edit configuration of flags and perms, and those can be added and removed at any time.
	@Override
	public List<MassiveCommand> getChildren()
	{
		List<MassiveCommand> ret = new MassiveList<>();
		for (O singleton : this.getAll())
		{
			MassiveCommand cmd = new CommandEditSingleton<>(singleton, this.getPermission());
			List<MassiveCommand> chain = this.getChain();
			chain.add(this);
			cmd.setChain(chain);
			ret.add(cmd);
		}
		return ret;
	}
	
}
