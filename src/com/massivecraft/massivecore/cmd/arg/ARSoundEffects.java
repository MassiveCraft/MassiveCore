package com.massivecraft.massivecore.cmd.arg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.util.Txt;

public class ARSoundEffects extends ArgReaderAbstract<List<SoundEffect>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ARSoundEffects i = new ARSoundEffects();
	public static ARSoundEffects get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public ArgResult<List<SoundEffect>> read(String arg, CommandSender sender)
	{
		ArgResult<List<SoundEffect>> ret = new ArgResult<List<SoundEffect>>();
		List<SoundEffect> result = new ArrayList<SoundEffect>();
		
		arg = arg.trim();
		List<String> soundStrings = Arrays.asList(arg.split("\\s+"));
		
		try
		{
			for (String soundString : soundStrings)
			{
				result.add(SoundEffect.valueOf(soundString));
			}
			ret.setResult(result);
		}
		catch (Exception e)
		{
			ret.setErrors(Txt.parse("<b>") + e.getMessage());
		}
		return ret;
	}
	
}
