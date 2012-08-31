package com.massivecraft.mcore4.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.massivecraft.mcore4.Predictate;

public class Persist
{
	public static List<Persist> instances = new CopyOnWriteArrayList<Persist>(); 
	
	private Map<Class<?>, IClassManager<?>> classManagers = new HashMap<Class<?>, IClassManager<?>>();
	public <T> void setManager(Class<T> clazz, IClassManager<T> manager)
	{
		this.classManagers.put(clazz, manager);
	};
	public Map<Class<?>, IClassManager<?>> getClassManagers()
	{
		return this.classManagers;
	}
	
	private Map<Class<?>, Timer> classSaveTimers = new HashMap<Class<?>, Timer>();
	public synchronized <T> void setSaveInterval(Class<T> clazz, long interval)
	{
		// Clear old timer
		Timer timer = this.classSaveTimers.get(clazz);
		if (timer != null)
		{
			timer.cancel();
			this.classSaveTimers.remove(clazz);
		}
		
		// Create new timer
		timer = new Timer();
		this.classSaveTimers.put(clazz, timer);
		
		// Add the task to the timer
		SaveTask<T> task = new SaveTask<T>(this, clazz);
		timer.scheduleAtFixedRate(task, interval, interval);
	};
	
	
	@SuppressWarnings("unchecked")
	public <T> IClassManager<T> getManager(Class<T> clazz)
	{
		return (IClassManager<T>) this.classManagers.get(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> IClassManager<T> getManager(T entity)
	{	
		return (IClassManager<T>) this.getManager(entity.getClass());
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Persist()
	{
		instances.add(this);
	}
	
	// -------------------------------------------- //
	// SAVE ALL
	// -------------------------------------------- //
	
	public void saveAll()
	{
		for (IClassManager<?> m : this.classManagers.values())
		{
			m.saveAll();
		}
	}
	
	// -------------------------------------------- //
	// UTILS
	// -------------------------------------------- //
	
	public static <T> ArrayList<T> uglySQL(Collection<T> items, Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset)
	{
		ArrayList<T> ret = new ArrayList<T>(items.size());
		
		// WHERE
		if (where == null)
		{
			ret.addAll(items);
		}
		else
		{
			for (T item : items)
			{
				if (where.apply(item))
				{
					ret.add(item);
				}
			}
		}
		
		// ORDERBY
		if (orderby != null)
		{
			Collections.sort(ret, orderby);
		}
		
		// LIMIT AND OFFSET
		// Parse args
		int fromIndex = 0;
		if (offset != null)
		{
			fromIndex = offset;
		}
		
		int toIndex = ret.size()-1;
		if (limit != null)
		{
			toIndex = offset+limit;
		}
		
		// Clean args
		if (fromIndex <= 0)
		{
			fromIndex = 0;
		}
		else if (fromIndex > ret.size()-1)
		{
			fromIndex = ret.size()-1;
		}
		
		if (toIndex < fromIndex)
		{
			toIndex = fromIndex;
		}
		else if (toIndex > ret.size()-1)
		{
			toIndex = ret.size()-1;
		}
		
		// No limit?
		if (fromIndex == 0 && toIndex == ret.size()-1) return ret;
		return new ArrayList<T>(ret.subList(fromIndex, toIndex));
	}
	
	// http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map, final boolean ascending)
	{
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
			new Comparator<Map.Entry<K,V>>()
			{
				@Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2)
				{
					int res;
					if (ascending)
					{
						res = e1.getValue().compareTo(e2.getValue());
					}
					else
					{
						res = e2.getValue().compareTo(e1.getValue());
					}
					return res != 0 ? res : 1;
				}
			}
		);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
}
