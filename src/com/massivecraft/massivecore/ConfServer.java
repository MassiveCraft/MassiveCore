package com.massivecraft.massivecore;

import java.util.Map;
import java.util.UUID;

import com.massivecraft.massivecore.util.MUtil;

public class ConfServer extends SimpleConfig
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static transient ConfServer i = new ConfServer();
	public static ConfServer get() { return i; }
	public ConfServer() { super(MassiveCore.get()); }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public static String serverid = UUID.randomUUID().toString();
	
	public static String dburi = "default";
	
	public static Map<String, String> alias2uri = MUtil.map(
		"default", "flatfile",
	    "flatfile", "flatfile://mstore",
	    "mongodb", "mongodb://localhost:27017/mstore"
	);
	
}
