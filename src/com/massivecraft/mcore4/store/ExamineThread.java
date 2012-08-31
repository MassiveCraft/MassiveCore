package com.massivecraft.mcore4.store;

public class ExamineThread<E, L> extends Thread
{
	protected Coll<E, L> coll; 
	
	public ExamineThread(Coll<E, L> coll)
	{
		this.coll = coll;
		this.setName("ExamineThread for "+coll.name());
	}
	
	// TODO: Implement logging and/or auto adjusting system for how long the sleep should be?
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				//long before = System.currentTimeMillis();
				
				coll.findSuspects();
				
				//long after = System.currentTimeMillis();
				
				//coll.mplugin().log(this.getName()+ " complete. Took "+ (after-before) +"ms.");
				
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
