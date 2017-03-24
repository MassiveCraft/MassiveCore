package com.massivecraft.massivecore.util;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.collections.MassiveSet;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * The ContainerUtil provides an imaginary super class to Collection and Map.
 * In Java they do not have a common interface yet many methods are similar and exists in both.
 * This some times results in twice the amount of source code, which we aim to remedy with this utility class.
 * 
 * We take an approach where we largely see a Map as a Collection of entries.
 * The "Container" class is simply an Object.
 * The return values are auto cast generics.
 * 
 * We have also added some information gatherers related to sorting and order. 
 */
public class ContainerUtil
{
	// -------------------------------------------- //
	// IS > CORE
	// -------------------------------------------- //
	
	public static boolean isContainer(Object container)
	{
		return isCollection(container) || isMap(container);
	}
	
	public static boolean isCollection(Object container)
	{
		return container instanceof Collection;
	}
	
	public static boolean isMap(Object container)
	{
		return container instanceof Map;
	}
	
	public static boolean isList(Object container)
	{
		return container instanceof List;
	}
	
	public static boolean isSet(Object container)
	{
		return container instanceof Set;
	}
	
	// -------------------------------------------- //
	// IS > BEHAVIOR
	// -------------------------------------------- //
	
	public static boolean isIndexed(Object container)
	{
		return isOrdered(container) || isSorted(container);
	}
	
	public static boolean isOrdered(Object container)
	{
		return container instanceof List || container instanceof LinkedHashMap || container instanceof LinkedHashSet;
	}
	
	public static boolean isSorted(Object container)
	{
		return container instanceof SortedSet || container instanceof SortedMap;
	}
	
	// -------------------------------------------- //
	// AS > CORE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <C extends Collection<?>> C asCollection(Object container)
	{
		if ( ! isCollection(container)) return null;
		return (C)container;
	}
	
	@SuppressWarnings("unchecked")
	public static <M extends Map<?, ?>> M asMap(Object container)
	{
		if ( ! isMap(container)) return null;
		return (M)container;
	}
	
	@SuppressWarnings("unchecked")
	public static <S extends Set<?>> S asSet(Object container)
	{
		if ( ! isSet(container)) return null;
		return (S)container;
	}
	
	@SuppressWarnings("unchecked")
	public static <L extends List<?>> L asList(Object container)
	{
		if ( ! isList(container)) return null;
		return (L)container;
	}
	
	// -------------------------------------------- //
	// METHODS > SIZE
	// -------------------------------------------- //
	
	public static boolean isEmpty(Object container)
	{
		if (container == null) throw new NullPointerException("container");
		
		Collection<Object> collection = asCollection(container);
		if (collection != null)
		{
			return collection.isEmpty();
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			return map.isEmpty();
		}
		
		throw new IllegalArgumentException(container.getClass().getName() + " is not a container.");
	}
	
	public static int size(Object container)
	{
		if (container == null) throw new NullPointerException("container");
		
		Collection<Object> collection = asCollection(container);
		if (collection != null)
		{
			return collection.size();
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			return map.size();
		}
		
		throw new IllegalArgumentException(container.getClass().getName() + " is not a container.");
	}
	
	// -------------------------------------------- //
	// METHODS > GET
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <E> Collection<E> getElements(Object container)
	{
		if (container == null) throw new NullPointerException("container");
		
		Collection<E> collection = asCollection(container);
		if (collection != null)
		{
			return collection;
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			return (Collection<E>) map.entrySet();
		}
		
		throw new IllegalArgumentException(container.getClass().getName() + " is not a container.");
	}
	
	// -------------------------------------------- //
	// METHODS > SET
	// -------------------------------------------- //
	
	public static void clear(Object container)
	{
		if (container == null) throw new NullPointerException("container");
		
		Collection<Object> collection = asCollection(container);
		if (collection != null)
		{
			collection.clear();
			return;
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			map.clear();
			return;
		}
		
		throw new IllegalArgumentException(container.getClass().getName() + " is not a container.");
	}
	
	public static void setElements(Object container, Iterable<?> elements)
	{
		clear(container);
		addElements(container, elements);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean addElement(Object container, Object element)
	{
		if (container == null) throw new NullPointerException("container");
		
		Collection<Object> collection = asCollection(container);
		if (collection != null)
		{
			return collection.add(element);
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			Entry<Object, Object> entry = (Entry<Object, Object>)element;
			Object key = entry.getKey();
			Object after = entry.getValue();
			Object before = map.put(key, after);
			return ! MUtil.equals(after, before);
		}
		
		throw new IllegalArgumentException(container.getClass().getName() + " is not a container.");
	}
	
	public static void addElements(Object container, Iterable<?> elements)
	{
		if (container == null) throw new NullPointerException("container");
		if (elements == null) throw new NullPointerException("elements");
		
		for (Object element : elements)
		{
			addElement(container, element);
		}
	}
	
	// -------------------------------------------- //
	// ADDITIONS & DELETIONS
	// -------------------------------------------- //
	
	public static <E> Collection<E> getAdditions(Object before, Object after)
	{
		Collection<E> elements = ContainerUtil.getElements(after);
		Set<E> ret = new MassiveSet<>(elements);
		ret.removeAll(ContainerUtil.getElements(before));
		return ret;
	}
	
	public static <E> Collection<E> getDeletions(Object before, Object after)
	{
		Collection<E> elements = ContainerUtil.getElements(before);
		Set<E> ret = new MassiveSet<>(elements);
		ret.removeAll(ContainerUtil.getElements(after));
		return ret;
	}
	
	// -------------------------------------------- //
	// COPY
	// -------------------------------------------- //

	// For this method we must make a distinction between list and set.
	@SuppressWarnings("unchecked")
	public static <V> V getCopy(V container)
	{
		List<Object> list = asList(container);
		if (list != null)
		{
			return (V) new MassiveList<>(list);
		}
		
		Set<Object> set = asSet(container);
		if (set != null)
		{
			return (V) new MassiveSet<>(set);
		}
		
		Collection<Object> collection = asCollection(container);
		if (collection != null)
		{
			// Use list as fallback, when neither list nor set.
			return (V) new MassiveList<>(collection);
		}
		
		Map<Object, Object> map = asMap(container);
		if (map != null)
		{
			return (V) new MassiveMap<>(map);
		}
		
		return null;
	}
	
}
