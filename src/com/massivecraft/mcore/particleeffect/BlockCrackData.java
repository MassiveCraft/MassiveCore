package com.massivecraft.mcore.particleeffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class is part of the ParticleEffect library and follows the same usage conditions
 * 
 * @author DarkBlade12
 */
public class BlockCrackData extends ParticleEffectData {
	private static final String FORMAT = "\\d+@\\d+(@\\d+(\\.\\d+)?){3}@\\d+";
	private final int id;
	private final byte data;

	public BlockCrackData(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
		super(offsetX, offsetY, offsetZ, amount);
		this.id = id;
		this.data = data;
	}

	public static BlockCrackData fromString(String s) throws IllegalArgumentException {
		if (!s.matches(FORMAT))
			throw new IllegalArgumentException("Invalid format");
		String[] p = s.split("@");
		return new BlockCrackData(Integer.parseInt(p[0]), Byte.parseByte(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]), Float.parseFloat(p[4]), Integer.parseInt(p[5]));
	}

	@Override
	public void displayEffect(Location center, Player... players) {
		ParticleEffect.displayBlockCrack(center, id, data, offsetX, offsetY, offsetZ, amount, players);
	}

	@Override
	public void displayEffect(Location center) {
		ParticleEffect.displayBlockCrack(center, id, data, offsetX, offsetY, offsetZ, amount);
	}

	@Override
	public void displayEffect(Location center, double range) {
		ParticleEffect.displayBlockCrack(center, range, id, data, offsetX, offsetY, offsetZ, amount);
	}

	public int getId() {
		return this.id;
	}

	public byte getData() {
		return this.data;
	}

	@Override
	public String toString() {
		return id + "@" + data + "@" + super.toString();
	}
}
