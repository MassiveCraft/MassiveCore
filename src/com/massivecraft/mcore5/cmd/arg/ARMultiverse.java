package com.massivecraft.mcore5.cmd.arg;

import java.util.Collection;

import com.massivecraft.mcore5.Permission;
import com.massivecraft.mcore5.cmd.MCommand;
import com.massivecraft.mcore5.usys.Multiverse;
import com.massivecraft.mcore5.usys.MultiverseColl;

public class ARMultiverse extends ARAbstractSelect<Multiverse>
{
	@Override
	public String typename()
	{
		return "multiverse";
	}

	@Override
	public Multiverse select(String str, MCommand mcommand)
	{
		return MultiverseColl.i.get(str);
	}
	
	@Override
	public boolean canList(MCommand mcommand)
	{
		return Permission.USYS_MULTIVERSE_LIST.has(mcommand.sender, false);
	}

	@Override
	public Collection<String> altNames(MCommand mcommand)
	{
		return MultiverseColl.i.getIds();
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ARMultiverse i = new ARMultiverse();
	public static ARMultiverse get() { return i; }
	
}
