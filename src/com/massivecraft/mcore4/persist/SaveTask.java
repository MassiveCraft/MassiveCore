package com.massivecraft.mcore4.persist;

import java.util.TimerTask;

public class SaveTask<T> extends TimerTask
{
	private Persist persist;
	
	private Class<T> clazz;
	public Class<T> getToBeSavedClass() { return clazz; }
	
	public SaveTask(Persist persist, Class<T> clazz)
	{
		this.persist = persist;
		this.clazz = clazz;
	}
	
	public SaveTask(Persist persist)
	{
		this(persist, null);
	}
	
	@Override
	public void run()
	{
		if (this.clazz == null)
		{
			this.persist.saveAll();
		}
		else
		{
			this.persist.getManager(this.clazz).saveAll();
		}
	}
}
