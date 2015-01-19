package com.massivecraft.massivecore.util;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class EventUtil
{
	public static Field fieldRegisteredListenerDotPriority;
	public static Field fieldRegisteredListenerDotListener;
	
	static
	{
		try
		{
			fieldRegisteredListenerDotPriority = RegisteredListener.class.getDeclaredField("priority");
			fieldRegisteredListenerDotPriority.setAccessible(true);
			
			fieldRegisteredListenerDotListener = RegisteredListener.class.getDeclaredField("listener");
			fieldRegisteredListenerDotListener.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void callEventAt(Event event, EventPriority priority)
	{
		HandlerList handlers = event.getHandlers();
		RegisteredListener[] listeners = handlers.getRegisteredListeners().clone();

		for (RegisteredListener registration : listeners)
		{
			try
			{
				EventPriority thisPriority = (EventPriority) fieldRegisteredListenerDotPriority.get(registration);
				if (thisPriority != priority) continue;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
			fireEventRegistration(event, registration);
		}
	}
	
	public static void callEventAfter(Event event, Listener listener, EventPriority priority)
	{
		HandlerList handlers = event.getHandlers();
		RegisteredListener[] listeners = handlers.getRegisteredListeners().clone();

		boolean run = false;
		for (RegisteredListener registration : listeners)
		{
			if (run)
			{
				fireEventRegistration(event, registration);
			}
			else
			{
				try
				{
					EventPriority thisPriority = (EventPriority) fieldRegisteredListenerDotPriority.get(registration);
					Listener thisListener = (Listener) fieldRegisteredListenerDotListener.get(registration);
					if (thisListener == listener && thisPriority == priority) run = true;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This is the for-loop part of SimplePluginManager#fireEvent
	 */
	public static void fireEventRegistration(Event event, RegisteredListener registration)
	{
		if (!registration.getPlugin().isEnabled()) return;

		try
		{
			registration.callEvent(event);
		}
		catch (AuthorNagException ex)
		{
			Plugin plugin = registration.getPlugin();

			if (plugin.isNaggable())
			{
				plugin.setNaggable(false);
		
				String author = "<NoAuthorGiven>";
		
				if (plugin.getDescription().getAuthors().size() > 0)
				{
					author = plugin.getDescription().getAuthors().get(0);
				}
				
				Bukkit.getServer().getLogger().log(Level.SEVERE, String.format("Nag author: '%s' of '%s' about the following: %s", author, plugin.getDescription().getName(), ex.getMessage()));
			}
		}
		catch (Throwable ex)
		{
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + registration.getPlugin().getDescription().getName(), ex);
		}
	}
}
