package com.massivecraft.massivecore.cmd.type;

import java.util.Collection;
import java.util.Set;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.SoundEffect;
import com.massivecraft.massivecore.collections.MassiveSet;

public class TypeSoundEffect extends TypeAbstractException<SoundEffect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeSoundEffect i = new TypeSoundEffect();
	public static TypeSoundEffect get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public SoundEffect valueOf(String arg, CommandSender sender) throws Exception
	{
		return SoundEffect.valueOf(arg);
	}
	
	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		Set<String> ret = new MassiveSet<String>();
		
		for (String str : TypeSound.get().getTabList(sender, arg))
		{
			ret.add(str + "-");
		}
		
		return ret;
	}
	
	@Override
	public boolean allowSpaceAfterTab()
	{
		return false;
	}

}
