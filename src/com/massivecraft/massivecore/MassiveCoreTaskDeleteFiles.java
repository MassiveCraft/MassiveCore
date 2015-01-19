package com.massivecraft.massivecore;

import java.io.File;

public class MassiveCoreTaskDeleteFiles implements Runnable
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveCoreTaskDeleteFiles i = new MassiveCoreTaskDeleteFiles();
	public static MassiveCoreTaskDeleteFiles get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		for (String deleteFile : MassiveCoreMConf.get().deleteFiles)
		{
			File file = new File(deleteFile);
			file.delete();
		}
	}
	
}