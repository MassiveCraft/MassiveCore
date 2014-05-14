package com.massivecraft.mcore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.Entity;

public class IdUpdateUtil
{
	// -------------------------------------------- //
	// SINGLE
	// -------------------------------------------- //
	
	public static String update(String string, boolean force)
	{
		if (string == null) return null;
		
		String ret = IdUtil.getId(string);
		if (ret != null) return ret;
		
		if (!force) return string;
		
		return null;
	}
	
	// -------------------------------------------- //
	// COLL
	// -------------------------------------------- //
	
	public static void update(Collection<String> from, Collection<String> to, boolean force)
	{
		for (String string : from)
		{
			string = update(string, force);
			if (string == null) continue;
			to.add(string);
		}
	}
	
	public static List<String> update(List<String> strings, boolean force)
	{
		List<String> ret = new ArrayList<String>();
		update(strings, ret, force);
		return ret;
	}
	
	public static Set<String> update(Set<String> strings, boolean force)
	{
		Set<String> ret = new LinkedHashSet<String>();
		update(strings, ret, force);
		return ret;
	}
	
	// -------------------------------------------- //
	// COLL
	// -------------------------------------------- //
	
	public static <E extends Entity<E>> void update(Coll<E> coll)
	{
		update(coll, false);
	}
	
	public static <E extends Entity<E>> void update(Coll<E> coll, boolean force)
	{
		long pre = System.currentTimeMillis();
		MCore.get().log(Txt.parse("<i>Pre update <h>%s<i>.", coll.getName()));
		
		int countTotal = coll.getAll().size();
		int countUpdate = 0;
		for (E entity : coll.getAll())
		{
			if (update(coll, entity, force)) countUpdate++;
		}
		
		long post = System.currentTimeMillis();
		long delta = post - pre;
		MCore.get().log(Txt.parse("<i>Post update <h>%s<i>. Took <h>%dms<i>. <h>%d/%d <i>changed.", coll.getName(), delta, countUpdate, countTotal));
	}
	
	public static <E extends Entity<E>> boolean update(Coll<E> coll, E entity, boolean force)
	{
		// Before and After
		String before = coll.getId(entity);
		if (before == null) return false;
		String after = update(before, force);
		if (after == null && !force) return false;
		
		// NoChange
		if (MUtil.equals(before, after)) return false;
		
		// Apply
		coll.detachEntity(entity);
		if (after == null) return true;
		coll.attach(entity, after);
		coll.syncId(after);
		
		return true;
	}
	
	
}
