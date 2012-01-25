package com.massivecraft.mcore2dev.persist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.massivecraft.mcore2dev.Predictate;

public class Persist
{
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
	
	public static void write(File file, String content) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
		out.write(content);
		out.close();
	}
	
	public static String read(File file) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String ret = new String(new byte[0], "UTF-8");
		 
		String line;
		while ((line = in.readLine()) != null)
		{
			ret += line;
		}

		in.close();
		return ret;
	}
	
	public static boolean writeCatch(File file, String content)
	{
		try
		{
			write(file, content);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static String readCatch(File file)
	{
		try
		{
			return read(file);
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	public static <T> ArrayList<T> uglySQL(Collection<T> items, Predictate<T> where, Comparator<T> orderby, Integer limit, Integer offset)
	{
		ArrayList<T> ret = new ArrayList<T>(items.size());
		
		// WHERE
		for (T item : items)
		{
			if (where.apply(item))
			{
				ret.add(item);
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
		
	public static String getBestCIStart(Collection<String> candidates, String start)
	{
		String ret = null;
		int best = 0;
		
		start = start.toLowerCase();
		int minlength = start.length();
		for (String candidate : candidates)
		{
			if (candidate.length() < minlength) continue;
			if ( ! candidate.toLowerCase().startsWith(start)) continue;
			
			// The closer to zero the better
			int lendiff = candidate.length() - minlength;
			if (lendiff == 0)
			{
				return candidate;
			}
			if (lendiff < best || best == 0)
			{
				best = lendiff;
				ret = candidate;
			}
		}
		return ret;
	}
}
