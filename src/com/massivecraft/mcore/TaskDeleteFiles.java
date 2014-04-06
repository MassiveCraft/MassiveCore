package com.massivecraft.mcore;

import java.io.File;

public class TaskDeleteFiles implements Runnable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskDeleteFiles i = new TaskDeleteFiles();
	public static TaskDeleteFiles get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		for (String deleteFile : MCoreConf.get().deleteFiles)
		{
			File file = new File(deleteFile);
			file.delete();
		}
	}
	
}