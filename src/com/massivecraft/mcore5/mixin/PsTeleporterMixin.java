package com.massivecraft.mcore5.mixin;

import org.bukkit.entity.Entity;

import com.massivecraft.mcore5.PS;

public interface PsTeleporterMixin
{
	/**
	 * @param entity The entity to be teleported
	 * @param ps The target PhysicalState
	 */
	public void teleport(Entity entity, PS ps) throws PsTeleporterException;
}
