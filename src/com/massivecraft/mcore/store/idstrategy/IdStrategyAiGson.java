package com.massivecraft.mcore.store.idstrategy;

import java.io.File;
import java.io.IOException;

import com.massivecraft.mcore.store.CollInterface;
import com.massivecraft.mcore.store.DbGson;
import com.massivecraft.mcore.util.DiscUtil;

public class IdStrategyAiGson extends IdStrategyAiAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static IdStrategyAiGson i = new IdStrategyAiGson();
	public static IdStrategyAiGson get() { return i;	}
	private IdStrategyAiGson() { super(); }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Integer getNextAndUpdate(CollInterface<?, String> coll)
	{
		Integer next = this.getNext(coll);
		if (next == null) return null;
		
		Integer newNext = next + 1;
		if (!this.setNext(coll, newNext)) return null;
		
		return next;
	}
	
	@Override
	public Integer getNext(CollInterface<?, String> coll)
	{
		File file = this.getAiFile(coll);
		if (this.ensureFileExists(file) == false) return null;
		String content = DiscUtil.readCatch(file);
		if (content == null) return null;
		Integer current = 0;
		if (content.length() > 0) current = Integer.valueOf(content);
		return current;
	}
	
	@Override
	public boolean setNext(CollInterface<?, String> coll, int next)
	{
		File file = this.getAiFile(coll);
		if (this.ensureFileExists(file) == false) return false;
		if (DiscUtil.writeCatch(file, String.valueOf(next)) == false) return false;
		return true;
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	private File getAiFile(CollInterface<?, String> coll)
	{
		DbGson cdb = (DbGson)coll.getDb();
		return new File(cdb.dir, coll.getName() + "_ai.txt");
	}
	
	private boolean ensureFileExists(File file)
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
	
}
