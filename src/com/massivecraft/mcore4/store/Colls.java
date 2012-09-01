package com.massivecraft.mcore4.store;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.mcore4.util.MUtil;

public abstract class Colls<C extends Coll<E, L>, E, L>
{
	public abstract String name();
	public abstract C createColl(String collName);
	
	public String collNameFromCategory(String category)
	{
		return this.name() + "_" + category;
	}
	
	protected Map<String, C> name2coll = new HashMap<String, C>();
	public C get(Object worldNameExtractable)
	{
		String worldName = MUtil.extract(String.class, "worldName", worldNameExtractable);
		String category = WCatColl.i.get(this.name()).categorize(worldName);
		String collName = this.collNameFromCategory(category);
		
		C ret = this.name2coll.get(collName);
		if (ret == null)
		{
			ret = this.createColl(collName);
			this.name2coll.put(collName, ret);
		}
		
		return ret;
	}
	
	public E get2(Object worldNameExtractable)
	{
		C coll = this.get(worldNameExtractable);
		if (coll == null) return null;
		return coll.get(worldNameExtractable);
	}
}
