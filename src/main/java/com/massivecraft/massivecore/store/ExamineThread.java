package com.massivecraft.massivecore.store;

public class ExamineThread extends Thread
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static ExamineThread i = null;
	public static ExamineThread get()
	{
		if (i == null || !i.isAlive()) i = new ExamineThread();
		return i;
	}
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ExamineThread()
	{
		this.setName("MStore ExamineThread");
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private long lastDurationMillis = 0;
	public long getLastDurationMillis() { return this.lastDurationMillis; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				long before = System.currentTimeMillis();
				for (Coll<?> coll : Coll.getInstances())
				{
					coll.findSuspects();
				}
				long after = System.currentTimeMillis();
				long duration = after-before;
				this.lastDurationMillis = duration;
				
				//String message = Txt.parse("<i>ExamineThread iteration took <h>%dms<i>.", after-before);
				//MassiveCore.get().log(message);
				
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				// We've been interrupted. Lets bail.
				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
