package com.massivecraft.massivecore.mixin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.permissions.Permissible;

public abstract class WorldMixinAbstract implements WorldMixin
{
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getVisibleWorldIds(Permissible permissible)
	{
		List<String> ret = new ArrayList<String>();
		for (String worldId : this.getWorldIds())
		{
			if (!this.canSeeWorld(permissible, worldId)) continue;
			ret.add(worldId);
		}
		return ret;
	}
	
	@Override
	public String getWorldAliasOrId(String worldId)
	{
		List<String> aliases = this.getWorldAliases(worldId);
		if (aliases.size() > 0) return aliases.get(0);
		return worldId;
	}

	@Override
	public String getWorldDisplayName(String worldId)
	{
		return this.getWorldColor(worldId).toString()+this.getWorldAliasOrId(worldId);
	}

}