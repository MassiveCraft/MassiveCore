package com.massivecraft.mcore4.store.idstrategy;

import java.io.File;
import java.io.IOException;

import com.massivecraft.mcore4.store.CollInterface;
import com.massivecraft.mcore4.store.DbGson;
import com.massivecraft.mcore4.util.DiscUtil;

public class IdStrategyAiGson extends IdStrategyAbstract<String, String>
{

	//----------------------------------------------//
	// CONSTRUCTORS
	//----------------------------------------------//
	
	private IdStrategyAiGson()
	{
		super("ai", String.class, String.class);
	}

	// -------------------------------------------- //
	// IMPLEMENTATION
	// -------------------------------------------- //
	
	@Override public String localToRemote(Object local) { return (String)local; }
	@Override public String remoteToLocal(Object remote) { return (String)remote; }
	
	@Override
	public String generateAttempt(CollInterface<?, String> coll)
	{
		File file = getAiFile(coll);

		// Ensure the file exists
		if (this.ensureFileExists(file) == false)
		{
			return null;
		}
		
		String content = DiscUtil.readCatch(file);
		if (content == null)
		{
			return null;
		}
		
		Integer current = 0;
		if (content.length() > 0)
		{
			current = Integer.valueOf(content);
		}
		
		Integer next = current + 1;
		if (DiscUtil.writeCatch(file, next.toString()) == false)
		{
			return null;
		}
		
		return current.toString();
		
	}
	
	protected File getAiFile(CollInterface<?, String> coll)
	{
		DbGson cdb = (DbGson)coll.getDb();
		return new File(cdb.dir, coll.getName() + "_ai.txt");
	}
	
	protected boolean ensureFileExists(File file)
	{
		if (file.isFile()) return true;
		if (file.isDirectory()) return false;
		try
		{
			return file.createNewFile();
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	protected static IdStrategyAiGson instance = new IdStrategyAiGson();
	public static IdStrategyAiGson get()
	{
		return instance;
	}
}
