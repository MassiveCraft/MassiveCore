package com.massivecraft.mcore5;

import org.bukkit.entity.Entity;

public interface PSTeleporter
{
	/**
	 * @param entity The entity to be teleported
	 * @param ps The target PhysicalState
	 */
	public void teleport(Entity entity, PS ps) throws PSTeleporterException;
}
