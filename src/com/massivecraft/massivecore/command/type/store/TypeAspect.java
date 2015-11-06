package com.massivecraft.massivecore.command.type.store;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.MassiveCorePerm;

public class TypeAspect extends TypeEntity<Aspect>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TypeAspect i = new TypeAspect();
	public static TypeAspect get() { return i; }
	public TypeAspect()
	{
		super(AspectColl.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean canList(CommandSender sender)
	{
		return MassiveCorePerm.USYS_ASPECT_LIST.has(sender, false);
	}
	
}
