package com.massivecraft.mcore4.store;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.util.MUtil;

public abstract class Colls<C extends Coll<E, L>, E, L>
{
	protected Map<String, C> name2coll = new HashMap<String, C>();
	
	public abstract C createColl(String name);
	
	public abstract String getContext();
	
	public String universeFromWorldName(String worldName)
	{
		USel selector = USelColl.i.get(this.getContext());
		return selector.select(worldName);
	}
	
	public C get(Object worldNameExtractable)
	{
		String worldName = MUtil.extract(String.class, "worldName", worldNameExtractable);
		return this.getForWorld(worldName);
	}
	
	public E get2(Object worldNameExtractable)
	{
		C coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(worldNameExtractable);
	}
	
	public C getForWorld(String worldName)
	{
		return this.getForUniverse(this.universeFromWorldName(worldName));
	}
	
	public C getForUniverse(String universe)
	{
		String collName = this.getContext() + "@" + universe;
		
		C ret = this.name2coll.get(collName);
		if (ret == null)
		{
			ret = this.createColl(collName);
			this.name2coll.put(collName, ret);
		}
		
		return ret;
	}
}
