package com.massivecraft.massivecore.cmd.arg;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;

public class ARAll<T> extends ARAbstract<Collection<T>>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private ARAllAble<T> innerArgReader;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ARAll(ARAllAble<T> inner)
	{
		if (inner == null) throw new IllegalArgumentException("innerArgReader musn't be null");
		this.innerArgReader = inner;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getTypeName()
	{
		return innerArgReader.getTypeName();
	}
	
	@Override
	public Collection<T> read(String arg, CommandSender sender) throws MassiveException
	{
		if ("all".equalsIgnoreCase(arg)) return innerArgReader.getAll();
		
		T innerRet = innerArgReader.read(arg, sender);
		
		List<T> ret = new MassiveList<T>();
		ret.add(innerRet);
		
		return ret;
	}

	@Override
	public boolean isValid(String arg, CommandSender sender)
	{
		return "all".equalsIgnoreCase(arg) || innerArgReader.isValid(arg, sender);
	}

	@Override
	public Collection<String> getTabList(CommandSender sender, String arg)
	{
		return innerArgReader.getTabList(sender, arg);
	}

}
