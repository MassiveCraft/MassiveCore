package com.massivecraft.massivecore.command.type.combined;

import java.util.List;

import org.bukkit.Sound;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.command.type.enumeration.TypeSound;
import com.massivecraft.massivecore.command.type.primitive.TypeFloat;

public class TypeSoundEffect extends TypeCombined<SoundEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSoundEffect i = new TypeSoundEffect();
	public static TypeSoundEffect get() { return i; }
	
	public TypeSoundEffect()
	{
		super(
			TypeSound.get(),
			TypeFloat.get(),
			TypeFloat.get()
		);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public List<Object> split(SoundEffect value)
	{
		return new MassiveList<Object>(
			value.getSound(),
			value.getVolume(),
			value.getPitch()
		);
	}
	
	@Override
	public SoundEffect combine(List<Object> parts)
	{
		Sound sound = null;
		float volume = 1.0f;
		float pitch = 1.0f;
		
		for (int i = 0 ; i < parts.size() ; i++)
		{
			Object part = parts.get(i);
			
			if (i == 0)
			{
				sound = (Sound)part;
			}
			else if (i == 1)
			{
				volume = (Float) part;
			}
			else if (i == 2)
			{
				pitch = (Float) part;
			}
		}
		
		return SoundEffect.valueOf(sound, volume, pitch);
	}
	
}
