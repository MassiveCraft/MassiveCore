package com.massivecraft.mcore5.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

@SuppressWarnings("unchecked")
public class ClassResolveUtil
{
	protected static Collection<JavaPluginLoader> javaPluginLoaders;
	
	static
	{
		try
		{
			SimplePluginManager pluginManager = (SimplePluginManager)Bukkit.getPluginManager();
			Field field = SimplePluginManager.class.getDeclaredField("fileAssociations");
			field.setAccessible(true);
			Map<Pattern, JavaPluginLoader> fileAssociations = (Map<Pattern, JavaPluginLoader>)field.get(pluginManager);
			javaPluginLoaders = fileAssociations.values(); // Does this one autoupdate?
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Class<?> resolve(String className) throws ClassNotFoundException
	{
		//System.out.println("resolve..."+this.javaPluginLoaders.toString());
		Class<?> ret = null;
		for (JavaPluginLoader javaPluginLoader : javaPluginLoaders)
		{
			ret = javaPluginLoader.getClassByName(className);
			if (ret != null)
			{
				return ret;
			}
		}
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
        try
        {
            return classLoader.loadClass(className);
        }
        catch (ClassNotFoundException e)
        {
            return Class.forName(className, false, classLoader);
        }
	}
}
