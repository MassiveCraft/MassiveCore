package com.massivecraft.mcore4;

import java.io.File;
import java.util.UUID;

import com.massivecraft.mcore4.util.DiscUtil;

public class Conf
{
	public static String dburi = "gson://./mstore";
	public static String serverid = UUID.randomUUID().toString();
	
	// -------------------------------------------- //
	// Persistance
	// -------------------------------------------- //
	private static transient File file = new File("plugins/mcore/conf.json");
	private static transient Conf i = new Conf();
	public static void load()
	{
		if (file.isFile())
		{
			String content = DiscUtil.readCatch(file);
			MCore.gson.fromJson(content, Conf.class);
		}
		save();
	}
	
	public static void save()
	{
		String content = MCore.gson.toJson(i, i.getClass());
		DiscUtil.writeCatch(file, content);
	}
}
