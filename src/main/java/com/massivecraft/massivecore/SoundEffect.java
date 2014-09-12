package com.massivecraft.massivecore;

import java.io.Serializable;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.cmd.arg.ARSound;

public final class SoundEffect implements Cloneable, Serializable
{
	private static final transient long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	private final Sound sound;
	public Sound getSound() { return this.sound; }
	
	private final float volume;
	public float getVolume() { return this.volume; }
	
	private final float pitch;
	public float getPitch() { return this.pitch; }
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public SoundEffect withSound(Sound sound) { return new SoundEffect(sound, volume, pitch); }
	public SoundEffect withVolume(float volume) { return new SoundEffect(sound, volume, pitch); }
	public SoundEffect withPitch(float pitch) { return new SoundEffect(sound, volume, pitch); }
	
	// -------------------------------------------- //
	// CONSTUCT
	// -------------------------------------------- //
	
	private SoundEffect(Sound sound, float volume, float pitch)
	{
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	private SoundEffect()
	{
		// No Arg Constructor for GSON
		this(null, 1.0f, 1.0f);
	}
	
	// -------------------------------------------- //
	// VALUE OF
	// -------------------------------------------- //
	
	public static SoundEffect valueOf(Sound sound, float volume, float pitch)
	{
		return new SoundEffect(sound, volume, pitch);
	}
	
	public static SoundEffect valueOf(String soundString) throws Exception
	{
		if (soundString == null) throw new NullPointerException("soundString was null");
		soundString = soundString.trim();
		
		String[] parts = soundString.split("[^a-zA-Z0-9_.]+");
		Sound sound = ARSound.getSoundFromString(parts[0]);
		if (sound == null) throw new IllegalArgumentException("Unknown sound \"" + parts[0] + "\"");
		
		float volume = 1.0f;
		if (parts.length >= 2)
		{
			volume = Float.parseFloat(parts[1]);
		}
		
		float pitch = 1.0f;
		if (parts.length >= 3)
		{
			pitch = Float.parseFloat(parts[2]);
		}
		
		return SoundEffect.valueOf(sound, volume, pitch);
	}
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	public void run(Location location)
	{
		location.getWorld().playSound(location, this.getSound(), this.getVolume(), this.getPitch());
	}
	
	public void run(Player player, Location location)
	{
		player.playSound(location, this.getSound(), this.getVolume(), this.getPitch());
	}
	
	public void run(Player player)
	{
		this.run(player, player.getEyeLocation());
	}
	
	// -------------------------------------------- //
	// RUN ALL
	// -------------------------------------------- //
	
	public static void runAll(Collection<SoundEffect> soundEffects, Location location)
	{
		for (SoundEffect soundEffect : soundEffects)
		{
			soundEffect.run(location);
		}
	}
	
	public static void runAll(Collection<SoundEffect> soundEffects, Player player, Location location)
	{
		for (SoundEffect soundEffect : soundEffects)
		{
			soundEffect.run(player, location);
		}
	}
	
	public static void runAll(Collection<SoundEffect> soundEffects, Player player)
	{
		for (SoundEffect soundEffect : soundEffects)
		{
			soundEffect.run(player);
		}
	}
	
	// -------------------------------------------- //
	// CLONE
	// -------------------------------------------- //
	
	@Override
	public SoundEffect clone()
	{
		return this;
	}
	
	// -------------------------------------------- //
	// EQUALS & HASHCODE
	// -------------------------------------------- //
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(pitch);
		result = prime * result + ((sound == null) ? 0 : sound.hashCode());
		result = prime * result + Float.floatToIntBits(volume);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof SoundEffect)) return false;
		SoundEffect other = (SoundEffect) obj;
		if (Float.floatToIntBits(pitch) != Float.floatToIntBits(other.pitch)) return false;
		if (sound != other.sound) return false;
		if (Float.floatToIntBits(volume) != Float.floatToIntBits(other.volume)) return false;
		return true;
	}
	
}
