package com.massivecraft.mcore.particleeffect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class is part of the ParticleEffect library and follows the same usage conditions
 * 
 * @author DarkBlade12
 */
public class NormalEffectData extends ParticleEffectData {
	private static final String FORMAT = "\\w+(@\\d+(\\.\\d+)?){3}@\\d+@\\d+(\\.\\d+)?";
	private final ParticleEffect effect;
	private final float speed;

	public NormalEffectData(ParticleEffect effect, float offsetX, float offsetY, float offsetZ, int amount, float speed) {
		super(offsetX, offsetY, offsetZ, amount);
		this.effect = effect;
		this.speed = speed;
	}

	public static NormalEffectData fromString(String s) throws IllegalArgumentException {
		if (!s.matches(FORMAT))
			throw new IllegalArgumentException("Invalid format");
		String[] p = s.split("@");
		ParticleEffect effect = ParticleEffect.fromName(p[0]);
		if (effect == null)
			throw new IllegalArgumentException("Contains an invalid particle effect name");
		return new NormalEffectData(effect, Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]), Integer.parseInt(p[4]), Float.parseFloat(p[5]));
	}

	@Override
	public void displayEffect(Location center, Player... players) {
		effect.display(center, offsetX, offsetY, offsetZ, speed, amount, players);
	}

	@Override
	public void displayEffect(Location center) {
		effect.display(center, offsetX, offsetY, offsetZ, speed, amount);
	}

	@Override
	public void displayEffect(Location center, double range) {
		effect.display(center, range, offsetX, offsetY, offsetZ, speed, amount);
	}

	public ParticleEffect getEffect() {
		return this.effect;
	}

	public float getSpeed() {
		return this.speed;
	}

	@Override
	public String toString() {
		return effect.getName() + "@" + super.toString() + "@" + speed;
	}
}
