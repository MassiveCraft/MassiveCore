package com.massivecraft.mcore.particleeffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class is part of the ParticleEffect library and follows the same usage conditions
 * 
 * @author DarkBlade12
 */
public class BlockDustData extends ParticleEffectData {
	private static final String FORMAT = "\\d+@\\d+(@\\d+(\\.\\d+)?){3}@\\d+@\\d+(\\.\\d+)?";
	private final int id;
	private final byte data;
	private final float speed;

	public BlockDustData(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, float speed) {
		super(offsetX, offsetY, offsetZ, amount);
		this.id = id;
		this.data = data;
		this.speed = speed;
	}

	public static BlockDustData fromString(String s) throws IllegalArgumentException {
		if (!s.matches(FORMAT))
			throw new IllegalArgumentException("Invalid format");
		String[] p = s.split("@");
		return new BlockDustData(Integer.parseInt(p[0]), Byte.parseByte(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]), Float.parseFloat(p[4]), Integer.parseInt(p[5]), Float.parseFloat(p[6]));
	}

	@Override
	public void displayEffect(Location center, Player... players) {
		ParticleEffect.displayBlockDust(center, id, data, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	@Override
	public void displayEffect(Location center) {
		ParticleEffect.displayBlockDust(center, id, data, offsetX, offsetY, offsetZ, speed, amount);
	}

	@Override
	public void displayEffect(Location center, double range) {
		ParticleEffect.displayBlockDust(center, range, id, data, offsetX, offsetY, offsetZ, speed, amount);
	}

	public int getId() {
		return this.id;
	}

	public byte getData() {
		return this.data;
	}

	public float getSpeed() {
		return this.speed;
	}

	@Override
	public String toString() {
		return id + "@" + data + "@" + super.toString() + "@" + speed;
	}
}
