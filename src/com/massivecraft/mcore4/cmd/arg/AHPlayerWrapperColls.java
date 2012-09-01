package com.massivecraft.mcore4.cmd.arg;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore4.MPlugin;
import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.Colls;
public class AHPlayerWrapperColls<E> extends AHBase<E>
{
	protected final Colls<Coll<E, ?>, E, ?> colls;
	public Colls<Coll<E, ?>, E, ?> colls() { return this.colls; };
	
	@SuppressWarnings("unchecked")
	public AHPlayerWrapperColls(Object colls)
	{
		this.colls = (Colls<Coll<E, ?>, E, ?>) colls;
	}
	
	@Override
	public E parse(String str, String style, CommandSender sender, MPlugin p)
	{
		Coll<E, ?> coll = colls.get(sender);
		AHPlayerWrapperColl<E> ahinner = new AHPlayerWrapperColl<E>(coll);
		E ret = ahinner.parse(str, style, sender, p);
		this.error.addAll(ahinner.error);
		return ret;
	}
}
