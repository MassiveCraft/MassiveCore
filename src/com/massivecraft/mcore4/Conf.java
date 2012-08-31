package com.massivecraft.mcore4;

import java.io.File;
import java.util.UUID;

public class Conf extends SimpleConfig
{
	// -------------------------------------------- //
	// CONTENT
	// -------------------------------------------- //
	
	public static String dburi = "gson://./mstore";
	public static String serverid = UUID.randomUUID().toString();
	
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	public static transient Conf i = new Conf();
	private Conf()
	{
		super(MCore.p, new File("plugins/mcore/conf.json"));
	}
}
