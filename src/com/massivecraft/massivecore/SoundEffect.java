package com.massivecraft.massivecore;

import java.io.Serializable;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.massivecraft.massivecore.command.type.enumeration.TypeSound;
import com.massivecraft.massivecore.util.MUtil;

public final class SoundEffect implements Serializable
{
	private static final transient long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	private final String soundId;
	public String getSoundId() { return this.soundId; }
	public Sound getSound()
	{
		return TypeSound.valueOf(this.getSoundId());
	}
	
	private final float volume;
	public float getVolume() { return this.volume; }
	
	private final float pitch;
	public float getPitch() { return this.pitch; }
	
	// -------------------------------------------- //
	// FIELDS: WITH
	// -------------------------------------------- //
	
	public SoundEffect withSoundId(String soundId) { return new SoundEffect(soundId, volume, pitch); }
	public SoundEffect withSound(Sound sound) { return new SoundEffect(TypeSound.get().getId(sound), volume, pitch); }
	public SoundEffect withVolume(float volume) { return new SoundEffect(soundId, volume, pitch); }
	public SoundEffect withPitch(float pitch) { return new SoundEffect(soundId, volume, pitch); }
	
	// -------------------------------------------- //
	// CONSTUCT
	// -------------------------------------------- //
	
	private SoundEffect(String soundId, float volume, float pitch)
	{
		this.soundId = soundId;
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
	
	public static SoundEffect valueOf(String soundId, float volume, float pitch)
	{
		return new SoundEffect(soundId, volume, pitch);
	}
	
	public static SoundEffect valueOf(Sound sound, float volume, float pitch)
	{
		return valueOf(TypeSound.get().getId(sound), volume, pitch);
	}
	
	// -------------------------------------------- //
	// RUN
	// -------------------------------------------- //
	
	public void run(Location location)
	{
		if (location == null) return;
		location.getWorld().playSound(location, this.getSound(), this.getVolume(), this.getPitch());
	}
	
	public void run(HumanEntity human, Location location)
	{
		if (MUtil.isntPlayer(human)) return;
		Player player = (Player)human;
		player.playSound(location, this.getSound(), this.getVolume(), this.getPitch());
	}
	
	public void run(HumanEntity human)
	{
		if (MUtil.isntPlayer(human)) return;
		this.run(human, human.getEyeLocation());
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
	
	public static void runAll(Collection<SoundEffect> soundEffects, HumanEntity human, Location location)
	{
		for (SoundEffect soundEffect : soundEffects)
		{
			soundEffect.run(human, location);
		}
	}
	
	public static void runAll(Collection<SoundEffect> soundEffects, HumanEntity human)
	{
		for (SoundEffect soundEffect : soundEffects)
		{
			soundEffect.run(human);
		}
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
		result = prime * result + ((soundId == null) ? 0 : soundId.hashCode());
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
		if (soundId == null)
		{
			if (other.soundId != null) return false;
		}
		else if (!soundId.equals(other.soundId)) return false;
		if (Float.floatToIntBits(volume) != Float.floatToIntBits(other.volume)) return false;
		return true;
	}
	
}
