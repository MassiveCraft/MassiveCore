package com.massivecraft.massivecore.comparator;

import java.util.List;

import com.massivecraft.massivecore.ActivePriority;
import com.massivecraft.massivecore.util.ReflectionUtil;

public class ComparatorActivePriority extends ComparatorAbstract<Class<?>>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static ComparatorActivePriority i = new ComparatorActivePriority();
	public static ComparatorActivePriority get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public int compareInner(Class<?> class1, Class<?> class2)
	{
		// Calculate priority
		int priority1 = getPriority(class1);
		int priority2 = getPriority(class2);

		// Compare
		return Integer.compare(priority1, priority2);
	}

	public static int getPriority(Class<?> clazz)
	{
		return getPriority(ReflectionUtil.getSuperclasses(clazz, true));
	}

	private static int getPriority(List<Class<?>> classes)
	{
		ActivePriority activePriority = getActivePriority(classes);
		if (activePriority == null) return 0;
		return activePriority.value();
	}

	private static ActivePriority getActivePriority(List<Class<?>> classes)
	{
		for (Class<?> clazz : classes)
		{
			ActivePriority activePriority = clazz.getAnnotation(ActivePriority.class);
			if (activePriority != null) return activePriority;
		}
		return null;
	}

}
