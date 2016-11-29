package com.massivecraft.massivecore.store;

public abstract class ModificationPollerAbstract extends Thread
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ModificationPollerAbstract()
	{
		this.setName("MStore " + this.getClass().getSimpleName());
	}
	
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
				//System.out.println("Polling locally: " + MassiveCoreMConf.get().millisBetweenLocalPoll);
				this.identify();
				
				//String message = Txt.parse("<i>LocalModificationThread iteration took <h>%dms<i>.", after-before);
				//MassiveCore.get().log(message);
				
				Thread.sleep(this.getMillisBetweenPoll());
			}
			catch (InterruptedException e)
			{
				// We've been interrupted. Lets bail.
				return;
			}
			catch (Exception e)
			{
				System.out.println("Poller error for" + this.getName());
				e.printStackTrace();
			}
		}
	}
	
	// -------------------------------------------- //
	// CORE
	// -------------------------------------------- //
	
	public void identify() throws InterruptedException
	{
		for (Coll<?> coll : Coll.getInstances())
		{
			this.poll(coll);
		}
	}
	
	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract long getMillisBetweenPoll();
	public abstract void poll(Coll<?> coll);
	
}
