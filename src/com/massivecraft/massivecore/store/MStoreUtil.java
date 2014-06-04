package com.massivecraft.massivecore.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.massivecraft.massivecore.Predictate;

public class MStoreUtil
{
	public static <T> ArrayList<T> uglySQL(Collection<T> items, Predictate<? super T> where, Comparator<? super T> orderby, Integer limit, Integer offset)
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
	
}
