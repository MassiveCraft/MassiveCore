package com.massivecraft.massivecore.util;

import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.comparator.ComparatorNaturalOrder;
import com.massivecraft.massivecore.predicate.Predicate;
import com.massivecraft.massivecore.predicate.PredicateAnd;
import com.massivecraft.massivecore.xlib.guava.reflect.ClassPath;
import com.massivecraft.massivecore.xlib.guava.reflect.ClassPath.ClassInfo;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReflectionUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	private static Field FIELD_DOT_MODIFIERS;
	
	static
	{
		try
		{
			FIELD_DOT_MODIFIERS = Field.class.getDeclaredField("modifiers");
			FIELD_DOT_MODIFIERS.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static final Class<?>[] EMPTY_ARRAY_OF_CLASS = {};
	private static final Object[] EMPTY_ARRAY_OF_OBJECT = {};
	
	// -------------------------------------------- //
	// MAKE ACCESSIBLE
	// -------------------------------------------- //
	
	public static void makeAccessible(Field field)
	{
		try
		{
			// Mark as accessible using reflection.
			field.setAccessible(true);
			
			// Remove the final modifier from the field.
			// http://stackoverflow.com/questions/2474017/using-reflection-to-change-static-final-file-separatorchar-for-unit-testing
			FIELD_DOT_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static void makeAccessible(Method method)
	{
		try
		{
			// Mark as accessible using reflection.
			method.setAccessible(true);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static void makeAccessible(Constructor<?> constructor)
	{
		try
		{
			// Mark as accessible using reflection.
			constructor.setAccessible(true);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	// -------------------------------------------- //
	// METHOD
	// -------------------------------------------- //
	
	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		try
		{
			Method ret = clazz.getDeclaredMethod(name, parameterTypes);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static boolean hasMethod(Class<?> clazz, String name)
	{
		return hasMethod(clazz, name, EMPTY_ARRAY_OF_CLASS);
	}
	
	public static boolean hasMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
	{
		try
		{
			getMethod(clazz, name, parameterTypes);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static Method getMethod(Class<?> clazz, String name)
	{
		return getMethod(clazz, name, EMPTY_ARRAY_OF_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object target, Object... arguments)
	{
		try
		{
			return (T) method.invoke(target, arguments);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static <T> T invokeMethod(Method method, Object target, Object argument)
	{
		return invokeMethod(method, target, new Object[]{argument});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object target)
	{
		return (T) invokeMethod(method, target, EMPTY_ARRAY_OF_OBJECT);
	}
	
	// -------------------------------------------- //
	// CONSTRUCTOR
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
	{
		try
		{	
			Constructor<T> ret = (Constructor<T>) clazz.getDeclaredConstructor(parameterTypes);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static <T> Constructor<T> getConstructor(Class<?> clazz)
	{
		return getConstructor(clazz, EMPTY_ARRAY_OF_CLASS);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeConstructor(Constructor<?> constructor, Object... arguments)
	{
		try
		{
			return (T) constructor.newInstance(arguments);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	public static <T> T invokeConstructor(Constructor<?> constructor, Object argument)
	{
		return invokeConstructor(constructor, new Object[]{argument});
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invokeConstructor(Constructor<?> constructor)
	{
		return (T) invokeConstructor(constructor, EMPTY_ARRAY_OF_OBJECT);
	}
	
	// -------------------------------------------- //
	// NEW INSTANCE
	// -------------------------------------------- //
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz)
	{
		try
		{
			return (T) clazz.newInstance();
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	// -------------------------------------------- //
	// SINGLETON INSTANCE
	// -------------------------------------------- //
	
	public static <T> T getSingletonInstance(Class<?> clazz)
	{
		Method get = getMethod(clazz, "get");
		T ret = invokeMethod(get, null);
		if (ret == null) throw new NullPointerException("Singleton instance was null for: " + clazz);
		if ( ! clazz.isAssignableFrom(ret.getClass())) throw new IllegalStateException("Singleton instance was not of same or subclass for: " + clazz);
		return ret;
	}
	
	public static <T> T getSingletonInstanceFirstCombatible(Iterable<Class<?>> classes, T fallback)
	{
		for (Class<?> c : classes)
		{
			try
			{
				return ReflectionUtil.getSingletonInstance(c);
			}
			catch (Throwable t)
			{
				// Not Compatible
			}
		}
		return fallback;
	}
	
	public static boolean isSingleton(Class<?> clazz)
	{
		try
		{
			Method get = getMethod(clazz, "get");
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	// -------------------------------------------- //
	// ANNOTATION
	// -------------------------------------------- //
	
	public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass)
	{
		// Fail Fast
		if (field == null) throw new NullPointerException("field");
		if (annotationClass == null) throw new NullPointerException("annotationClass");
		
		try
		{
			return field.getAnnotation(annotationClass);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			return null;
		}
	}
	
	// -------------------------------------------- //
	// FIELD > GET
	// -------------------------------------------- //
	
	public static Field getField(Class<?> clazz, String name)
	{
		if (clazz == null) throw new NullPointerException("clazz");
		if (name == null) throw new NullPointerException("name");
		try
		{
			Field ret = clazz.getDeclaredField(name);
			makeAccessible(ret);
			return ret;
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getField(Field field, Object object)
	{
		try
		{
			return (T) field.get(object);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	// -------------------------------------------- //
	// FIELD > SET
	// -------------------------------------------- //
	
	public static void setField(Field field, Object object, Object value)
	{
		try
		{
			field.set(object, value);
		}
		catch (Exception e)
		{
			throw asRuntimeException(e);
		}
	}
	
	// -------------------------------------------- //
	// FIELD > SIMPLE
	// -------------------------------------------- //
	
	public static <T> T getField(Class<?> clazz, String name, Object object)
	{
		Field field = getField(clazz, name);
		return getField(field, object);
	}
	
	public static void setField(Class<?> clazz, String name, Object object, Object value)
	{
		Field field = getField(clazz, name);
		setField(field, object, value);
	}
	
	// -------------------------------------------- //
	// FIELD > TRANSFER
	// -------------------------------------------- //
	
	public static void transferField(Class<?> clazz, Object from, Object to, String name)
	{
		Field field = getField(clazz, name);
		Object value = getField(field, from);
		setField(field, to, value);
	}
	
	public static void transferFields(Class<?> clazz, Object from, Object to, List<String> fieldNames)
	{
		if (fieldNames == null)
		{
			fieldNames = new ArrayList<>();
			for (Field field : clazz.getDeclaredFields())
			{
				fieldNames.add(field.getName());
			}
		}
		
		for (String fieldName : fieldNames)
		{
			transferField(clazz, from, to, fieldName);
		}
	}
	
	public static void transferFields(Class<?> clazz, Object from, Object to)
	{
		transferFields(clazz, from, to, null);
	}
	
	// -------------------------------------------- //
	// SUPERCLASSES
	// -------------------------------------------- //
	
	public static List<Class<?>> getSuperclasses(Class<?> clazz, boolean includeSelf)
	{
		// Create
		List<Class<?>> ret = new ArrayList<>();
		
		// Fill
		if ( ! includeSelf) clazz = clazz.getSuperclass();
		while (clazz != null)
		{
			ret.add(clazz);
			clazz = clazz.getSuperclass();
		}
		
		// Return
		return ret;
	}
	
	public static Class<?> getSuperclassPredicate(Class<?> clazz, boolean includeSelf, Predicate<Class<?>> predicate)
	{
		for (Class<?> superClazz : getSuperclasses(clazz, includeSelf))
		{
			if (predicate.apply(superClazz)) return superClazz;
		}
		return null;
	}
	
	public static Class<?> getSuperclassDeclaringMethod(Class<?> clazz, boolean includeSelf, final String methodName)
	{
		return getSuperclassPredicate(clazz, includeSelf, new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> clazz)
			{
				for (Method method : clazz.getDeclaredMethods())
				{
					if (method.getName().equals(methodName)) return true;
				}
				return false;
			}
		});
	}
	
	public static Class<?> getSuperclassDeclaringField(Class<?> clazz, boolean includeSelf, final String fieldName)
	{
		return getSuperclassPredicate(clazz, includeSelf, new Predicate<Class<?>>()
		{
			@Override
			public boolean apply(Class<?> clazz)
			{
				for (Field field : clazz.getDeclaredFields())
				{
					if (field.getName().equals(fieldName)) return true;
				}
				return false;
			}
		});
	}

	// -------------------------------------------- //
	// GET PACKAGE CLASSES
	// -------------------------------------------- //

	@SuppressWarnings("unchecked")
	public static List<Class<?>> getPackageClasses(String packageName, ClassLoader classLoader, boolean recursive, Predicate<Class<?>>... predicates)
	{
		// Create ret
		List<Class<?>> ret = new MassiveList<>();

		try
		{
			// Get info
			ClassPath classPath = ClassPath.from(classLoader);
			Predicate<Class<?>> predicateCombined = PredicateAnd.get(predicates);

			Collection<ClassInfo> classInfos = recursive ? classPath.getTopLevelClassesRecursive(packageName) : classPath.getTopLevelClasses(packageName);

			for (ClassInfo classInfo : classInfos)
			{
				// Get name of class
				String className = classInfo.getName();

				// Avoid versions created at runtime
				// Apparently it found a "EngineMassiveCoreCollTick 3" which we don't want
				if (className.contains(" ")) continue;

				// Try and load it
				Class<?> clazz;
				try
				{
					clazz = classInfo.load();
				}
				catch (NoClassDefFoundError ex)
				{
					// This thing couldn't be loaded. Probably has to do with integrations.
					// Just skip it
					continue;
				}

				// And it must not be ignored
				if (!predicateCombined.apply(clazz)) continue;

				ret.add(clazz);
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
		
		Collections.sort(ret, new Comparator<Class<?>>()
		{
			@Override
			public int compare(Class<?> class1, Class<?> class2)
			{
				return ComparatorNaturalOrder.get().compare(class1.getName(), class2.getName());
			}
		});

		return ret;
	}

	// -------------------------------------------- //
	// AS RUNTIME EXCEPTION
	// -------------------------------------------- //
	
	public static RuntimeException asRuntimeException(Throwable t)
	{
		// Runtime
		if (t instanceof RuntimeException) return (RuntimeException) t;
		
		// Invocation
		if (t instanceof InvocationTargetException) return asRuntimeException(((InvocationTargetException)t).getCause());
		
		// Rest
		return new IllegalStateException(t.getClass().getSimpleName() + ": " + t.getMessage());
	}
	
	// -------------------------------------------- //
	// BUKKIT VERSION
	// -------------------------------------------- //
	
	// Example: "v1_9_R4"
	private static String versionRaw = Bukkit.getServer().getClass().getPackage().getName().substring(23);
	public static String getVersionRaw() { return versionRaw; }
	
	public static String getVersionRawPart(int index)
	{
		String versionRaw = getVersionRaw();
		String[] parts = versionRaw.split("_");
		return parts[index];
	}
	
	// Example: 1
	private static int versionMajor = Integer.valueOf(getVersionRawPart(0).substring(1));
	public static int getVersionMajor() { return versionMajor; }
	
	// Example: 9
	private static int versionMinor = Integer.valueOf(getVersionRawPart(1));
	public static int getVersionMinor() { return versionMinor; }
	
	// Example: 4
	private static int versionRelease = Integer.valueOf(getVersionRawPart(2).substring(1));
	public static int getVersionRelease() { return versionRelease; }
	
	// -------------------------------------------- //
	// FORCE LOAD CLASSES
	// -------------------------------------------- //
	
	public static void forceLoadClasses(Class<?>... classes)
	{
		for (Class<?> clazz : classes)
		{
			forceLoadClass(clazz);
		}
	}
	
	// We chose this "weird" string in startsWith to avoid getting optimized away by the JIT compiler.
	// It will never start with this string, but the JIT can't be sure of that.
	public static void forceLoadClass(Class<?> clazz)
	{
		String className = clazz.getSimpleName();
		if (className.startsWith("Spaces are not allowed in class names."))
		{
			System.out.println(className);
		}
	}

	// -------------------------------------------- //
	// CLASS EXISTENCE
	// -------------------------------------------- //

	public static boolean classExists(String className)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (ClassNotFoundException ex)
		{
			return false;
		}
	}
	
	// -------------------------------------------- //
	// TYPE CHECKS
	// -------------------------------------------- //
	
	public static boolean isRawTypeAssignableFromAny(Type goal, Type... subjects)
	{
		// Cache this value since it will save us calculations
		Class<?> classGoal = classify(goal);
		
		for (Type t: subjects)
		{
			if (isRawTypeAssignableFrom(classGoal, t)) return true;
		}
		return false;
	}
	
	public static boolean isRawTypeAssignableFrom(Type a, Type b)
	{
		if (a == null || b == null) return false;
		
		// Yes, this is a different sense of "Classifying"
		Class<?> classifiedA = classify(a);
		Class<?> classifiedB = classify(b);
		
		// In case one of the methods failed to retrieve a class
		if (classifiedA == null || classifiedB == null) return a.equals(b);
		
		return classifiedA.isAssignableFrom(classifiedB);
	}
	
	private static Class<?> classify(Type type)
	{
		// Use loop structure rather than recursion to avoid stack size issues
		while (!(type instanceof Class))
		{
			// Check for parameterized type
			if (!(type instanceof ParameterizedType)) return null;
			type = ((ParameterizedType) type).getRawType();
		}
		return (Class) type;
	}
	
}
