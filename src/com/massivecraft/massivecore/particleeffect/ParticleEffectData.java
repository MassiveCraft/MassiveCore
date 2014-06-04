package com.massivecraft.massivecore.particleeffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class is part of the ParticleEffect library and follows the same usage conditions
 * 
 * @author DarkBlade12
 */
public abstract class ParticleEffectData {
	protected final float offsetX, offsetY, offsetZ;
	protected final int amount;

	public ParticleEffectData(float offsetX, float offsetY, float offsetZ, int amount) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.amount = amount;
	}

	public abstract void displayEffect(Location center, Player... players);

	public abstract void displayEffect(Location center);

	public abstract void displayEffect(Location center, double range);

	public float getOffsetX() {
		return this.offsetX;
	}

	public float getOffsetY() {
		return this.offsetY;
	}

	public float getOffsetZ() {
		return this.offsetZ;
	}

	public int getAmount() {
		return this.amount;
	}

	@Override
	public String toString() {
		return offsetX + "@" + offsetY + "@" + offsetZ + "@" + amount;
	}
}
