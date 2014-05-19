package com.massivecraft.mcore.particleeffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class is part of the ParticleEffect library and follows the same usage conditions
 * 
 * @author DarkBlade12
 */
public class IconCrackData extends ParticleEffectData {
	private static final String FORMAT = "\\d+(@\\d+(\\.\\d+)?){3}@\\d+@\\d+(\\.\\d+)?";
	private final int id;
	private final float speed;

	public IconCrackData(int id, float offsetX, float offsetY, float offsetZ, int amount, float speed) {
		super(offsetX, offsetY, offsetZ, amount);
		this.id = id;
		this.speed = speed;
	}

	public static IconCrackData fromString(String s) throws IllegalArgumentException {
		if (!s.matches(FORMAT))
			throw new IllegalArgumentException("Invalid format");
		String[] p = s.split("@");
		return new IconCrackData(Integer.parseInt(p[0]), Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]), Integer.parseInt(p[4]), Float.parseFloat(p[5]));
	}

	@Override
	public void displayEffect(Location center, Player... players) {
		ParticleEffect.displayIconCrack(center, id, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	@Override
	public void displayEffect(Location center) {
		ParticleEffect.displayIconCrack(center, id, offsetX, offsetY, offsetZ, speed, amount);
	}

	@Override
	public void displayEffect(Location center, double range) {
		ParticleEffect.displayIconCrack(center, range, id, offsetX, offsetY, offsetZ, speed, amount);
	}

	public int getId() {
		return this.id;
	}

	public float getSpeed() {
		return this.speed;
	}

	@Override
	public String toString() {
		return id + "@" + super.toString() + "@" + speed;
	}
}
